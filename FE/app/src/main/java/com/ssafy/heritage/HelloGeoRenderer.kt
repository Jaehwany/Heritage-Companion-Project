/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssafy.heritage

import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Anchor
import com.google.ar.core.Point
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import com.ssafy.heritage.helpers.DisplayRotationHelper
import com.ssafy.heritage.helpers.TrackingStateHelper
import com.ssafy.heritage.samplerender.*
import com.ssafy.heritage.samplerender.arcore.BackgroundRenderer
import java.io.IOException


private const val TAG = "HelloGeoRenderer___"
class HelloGeoRenderer(val activity: HelloGeoActivity) :  SampleRender.Renderer, DefaultLifecycleObserver {
  companion object {
    private val Z_NEAR = 0.1f
    private val Z_FAR = 1000f
  }
  var touchLat: Double = 0.0
  var touchLong: Double = 0.0

  lateinit var backgroundRenderer: BackgroundRenderer
  lateinit var virtualSceneFramebuffer: Framebuffer
  var hasSetTextureNames = false

  var anchorNode: AnchorNode? =null
  var earthAnchor: Anchor? = null


  // Virtual object (ARCore pawn)
  lateinit var virtualObjectMesh: Mesh
  lateinit var virtualObjectShader: Shader
  lateinit var virtualObjectTexture: Texture

  // Temporary matrix allocated here to reduce number of allocations for each frame.
  val modelMatrix = FloatArray(16)
  val viewMatrix = FloatArray(16)
  val projectionMatrix = FloatArray(16)
  val modelViewMatrix = FloatArray(16) // view x model

  val modelViewProjectionMatrix = FloatArray(16) // projection x view x model

  val session
    get() = activity.arCoreSessionHelper.session

  val displayRotationHelper = DisplayRotationHelper(activity)
  val trackingStateHelper = TrackingStateHelper(activity)



  override fun onResume(owner: LifecycleOwner) {
    displayRotationHelper.onResume()
    hasSetTextureNames = false
  }

  override fun onPause(owner: LifecycleOwner) {
    displayRotationHelper.onPause()
  }

  override fun onSurfaceCreated(render: SampleRender) {
    // Prepare the rendering objects.
    // This involves reading shaders and 3D model files, so may throw an IOException.
    try {
      backgroundRenderer = BackgroundRenderer(render)
      virtualSceneFramebuffer = Framebuffer(render, /*width=*/ 1, /*height=*/ 1)

      // Virtual object to render (Geospatial Marker)
      virtualObjectTexture =
        Texture.createFromAsset(
          render,
          "models/spatial_marker_baked.png",
          Texture.WrapMode.CLAMP_TO_EDGE,
          Texture.ColorFormat.SRGB
        )

      virtualObjectMesh = Mesh.createFromAsset(render, "models/geospatial_marker.obj");
      virtualObjectShader =
        Shader.createFromAssets(
          render,
          "shaders/ar_unlit_object.vert",
          "shaders/ar_unlit_object.frag",
          /*defines=*/ null)
          .setTexture("u_Texture", virtualObjectTexture)

      backgroundRenderer.setUseDepthVisualization(render, false)
      backgroundRenderer.setUseOcclusion(render, false)
    } catch (e: IOException) {
      Log.e(TAG, "Failed to read a required asset file", e)
      showError("Failed to read a required asset file: $e")
    }
  }

  override fun onSurfaceChanged(render: SampleRender, width: Int, height: Int) {
    displayRotationHelper.onSurfaceChanged(width, height)
    virtualSceneFramebuffer.resize(width, height)
  }
  //</editor-fold>

  override fun onDrawFrame(render: SampleRender) {
    val session = session ?: return

    //<editor-fold desc="ARCore frame boilerplate" defaultstate="collapsed">
    // Texture names should only be set once on a GL thread unless they change. This is done during
    // onDrawFrame rather than onSurfaceCreated since the session is not guaranteed to have been
    // initialized during the execution of onSurfaceCreated.
    if (!hasSetTextureNames) {
      session.setCameraTextureNames(intArrayOf(backgroundRenderer.cameraColorTexture.textureId))
      hasSetTextureNames = true

    }


    // -- Update per-frame state

    // Notify ARCore session that the view size changed so that the perspective matrix and
    // the video background can be properly adjusted.
    displayRotationHelper.updateSessionIfNeeded(session)

    // Obtain the current frame from ARSession. When the configuration is set to
    // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
    // camera framerate.
    val frame =
      try {
        session.update()
      } catch (e: CameraNotAvailableException) {
        Log.e(TAG, "Camera not available during onDrawFrame", e)
        showError("Camera not available. Try restarting the app.")
        return
      }

    val camera = frame.camera

    // BackgroundRenderer.updateDisplayGeometry must be called every frame to update the coordinates
    // used to draw the background camera image.
    backgroundRenderer.updateDisplayGeometry(frame)

    // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
    trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

    // -- Draw background
    if (frame.timestamp != 0L) {
      // Suppress rendering if the camera did not produce the first frame yet. This is to avoid
      // drawing possible leftover data from previous sessions if the texture is reused.
      backgroundRenderer.drawBackground(render)
    }

    // If not tracking, don't draw 3D objects.
    if (camera.trackingState == TrackingState.PAUSED) {
      return
    }

    // Get projection matrix.
    camera.getProjectionMatrix(projectionMatrix, 0, Z_NEAR, Z_FAR)

    // Get camera matrix and draw.
    camera.getViewMatrix(viewMatrix, 0)

    render.clear(virtualSceneFramebuffer, 0f, 0f, 0f, 0f)
    //</editor-fold>

    // 어스 객체를 사용할 수 있는지 확인
    val earth = session.earth
    if (earth?.trackingState == TrackingState.TRACKING) {
      // 위도와 경도로 표현된 위치, 정확성에 대한 추정치도 제공
      // 고도와 고고의 추정치
      // 방향, 기기의 현재 방향에 대한 근사치와 방향의 추정치
      val cameraGeospatialPose = earth.cameraGeospatialPose
      activity.view.mapView?.updateMapPosition(
        latitude = cameraGeospatialPose.latitude,
        longitude = cameraGeospatialPose.longitude,
        heading = cameraGeospatialPose.heading
      )
    }

    // Draw the placed anchor, if it exists.
    earthAnchor?.let {
      render.renderCompassAtAnchor(it)
    }
    // Compose the virtual scene with the background.
    backgroundRenderer.drawVirtualScene(render, virtualSceneFramebuffer, Z_NEAR, Z_FAR)
    activity.view.surfaceView.setOnTouchListener { view, motionEvent ->
      if(earthAnchor == null){
        Log.d(TAG, "&&NODE 어디갔냐!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
      } else{
        Log.d(TAG, "&&NODE 여깃냐!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        Log.d(TAG, "&&earthAnchor_x: ${earthAnchor?.pose?.xAxis?.get(0)}")
        Log.d(TAG, "&&earthAnchor_x_1: ${earthAnchor?.pose?.xAxis?.get(1)}")
        Log.d(TAG, "&&earthAnchor_x_q: ${earthAnchor?.pose?.qx()}")
        Log.d(TAG, "&&motionEvent_xPrecision: ${motionEvent}")
        Log.d(TAG, "&&motionEvent_xPrecision: ${motionEvent.xPrecision}")
        Log.d(TAG, "&&getAxisValue_x: ${motionEvent.getAxisValue(0)}")
        Log.d(TAG, "&&getAxisValue_rawX: ${motionEvent.rawX}")
        Log.d(TAG, "&&getAxisValue_getX0: ${motionEvent.getX(0)}")
        Log.d(TAG, "&&getAxisValue_getX: ${motionEvent.x}")

        Log.d(TAG, "&&NODE YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")

        Log.d(TAG, "&&earthAnchor_y: ${earthAnchor?.pose?.yAxis?.get(0)}")
        Log.d(TAG, "&&earthAnchor_y_1: ${earthAnchor?.pose?.yAxis?.get(1)}")
        Log.d(TAG, "&&motionEvent_yPrecision: ${motionEvent.yPrecision}")
        Log.d(TAG, "&&getAxisValue_y: ${motionEvent.getAxisValue(1)}")
        Log.d(TAG, "&&getAxisValue_rawY: ${motionEvent.rawY}")
        Log.d(TAG, "&&getAxisValue_getY0: ${motionEvent.getY(0)}")
        Log.d(TAG, "&&getAxisValue_getY: ${motionEvent.y}")


        session.earth?.anchors?.remove(earthAnchor)
      }
      true
    }

  }


  fun onMapClick(latLng: LatLng) {
    val earth = session?.earth ?: return
    // 객체가 추적 중인지 확인
    if (earth.trackingState != TrackingState.TRACKING) {
      return
    }
    // earthAnchor가 있으면 분리
    earthAnchor?.detach()

    // (임시) 어스 앵커를 카메라와 같은 높이에 둔다
    val altitude = earth.cameraGeospatialPose.altitude - 1
    // 좌표계에서 앵커의 회전 방향
    val qx = 0f
    val qy = 0f
    val qz = 0f
    val qw = 1f
    Log.d(TAG, "666${earthAnchor?.pose}")
    Log.d(TAG, "777${anchorNode?.anchor?.pose}")
    earthAnchor =
      earth.createAnchor(latLng.latitude, latLng.longitude, altitude, qx, qy, qz, qw)

    anchorNode = AnchorNode(earthAnchor)

    anchorNode?.anchor=earth.createAnchor(latLng.latitude, latLng.longitude, altitude, qx, qy, qz, qw)
    touchLat = latLng.latitude
    touchLong= latLng.longitude


    Log.d(TAG, "8888${earthAnchor?.pose}")

    Log.d(TAG, "9999${anchorNode?.anchor?.pose}")
    activity.view.mapView?.earthMarker?.apply {
      position = latLng
      isVisible = true
    }
//    activity.view.surfaceView.setOnTouchListener { view, motionEvent ->
//      val x = earthAnchor?.pose?.xAxis?.get(0)?.toInt()
//      Log.d(TAG, "&&onMapClick_x: ${earthAnchor?.pose?.xAxis?.get(0)}")
//      Log.d(TAG, "&&motionEvent_xPrecision: ${motionEvent.xPrecision}")
//
//      Log.d(TAG, "&&onMapClick_y: ${earthAnchor?.pose?.yAxis?.get(1)}")
//      Log.d(TAG, "&&motionEvent_yPrecision: ${motionEvent.yPrecision}")
//      earthAnchor?.detach()
//      true
//    }

  }



  private fun SampleRender.renderCompassAtAnchor(anchor: Anchor) {
    // Get the current pose of the Anchor in world space. The Anchor pose is updated
    // during calls to session.update() as ARCore refines its estimate of the world.
    anchor.pose.toMatrix(modelMatrix, 0)
   // Log.d(TAG, "renderCompassAtAnchor: ${anchor.pose}")
   // Log.d(TAG, "modelMatrix: ${modelMatrix}")
    // Calculate model/view/projection matrices
    Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
    Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)

    // Update shader properties and draw
    virtualObjectShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)
    draw(virtualObjectMesh, virtualObjectShader, virtualSceneFramebuffer)
    //Log.d(TAG, " modelViewProjectionMatrix: ${modelViewProjectionMatrix}")

  }

  private fun showError(errorMessage: String) =
    activity.view.snackbarHelper.showError(activity, errorMessage)



}
