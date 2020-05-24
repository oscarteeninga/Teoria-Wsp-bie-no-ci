//
//  ViewController.swift
//  camera-object-detector
//
//  Created by Oscar Teeninga on 17/12/2019.
//  Copyright © 2019 Oscar Teeninga. All rights reserved.
//
import UIKit
import AVKit
import Vision

class ViewController: UIViewController, AVCaptureVideoDataOutputSampleBufferDelegate {

    let videoQueue = DispatchQueue(label: "VIDEO_QUEUE")
    let count = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let captureSession = AVCaptureSession()
        guard let captureDevice = AVCaptureDevice.default(for: .video) else { return }
                        
        guard let input = try? AVCaptureDeviceInput(device: captureDevice) else { return }
        captureSession.addInput(input)
        
        captureSession.startRunning()
        
        let previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        
        view.layer.addSublayer(previewLayer)
        previewLayer.frame = view.frame
        
        let dataOutput = AVCaptureVideoDataOutput()
        
        dataOutput.setSampleBufferDelegate(self, queue: videoQueue)
        captureSession.addOutput(dataOutput)
    }

    public func captureOutput(_ captureOutput: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
        let ts:CMTime = CMSampleBufferGetPresentationTimeStamp(sampleBuffer)
        let mycapturedimage = imageFromSampleBuffer(sampleBuffer: sampleBuffer)
    }
    
    func imageFromSampleBuffer(sampleBuffer : CMSampleBuffer) -> UIImage
    {
      // Get a CMSampleBuffer's Core Video image buffer for the media data
      let  imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer);
      // Lock the base address of the pixel buffer
      CVPixelBufferLockBaseAddress(imageBuffer!, CVPixelBufferLockFlags.readOnly);


      // Get the number of bytes per row for the pixel buffer
      let baseAddress = CVPixelBufferGetBaseAddress(imageBuffer!);

      // Get the number of bytes per row for the pixel buffer
      let bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer!);
      // Get the pixel buffer width and height
      let width = CVPixelBufferGetWidth(imageBuffer!);
      let height = CVPixelBufferGetHeight(imageBuffer!);

      // Create a device-dependent RGB color space
      let colorSpace = CGColorSpaceCreateDeviceRGB();

      // Create a bitmap graphics context with the sample buffer data
      var bitmapInfo: UInt32 = CGBitmapInfo.byteOrder32Little.rawValue
      bitmapInfo |= CGImageAlphaInfo.premultipliedFirst.rawValue & CGBitmapInfo.alphaInfoMask.rawValue
      //let bitmapInfo: UInt32 = CGBitmapInfo.alphaInfoMask.rawValue
      let context = CGContext.init(data: baseAddress, width: width, height: height, bitsPerComponent: 8, bytesPerRow: bytesPerRow, space: colorSpace, bitmapInfo: bitmapInfo)
      // Create a Quartz image from the pixel data in the bitmap graphics context
      let quartzImage = context?.makeImage();
      // Unlock the pixel buffer
      CVPixelBufferUnlockBaseAddress(imageBuffer!, CVPixelBufferLockFlags.readOnly);

      // Create an image object from the Quartz image
      let image = UIImage.init(cgImage: quartzImage!);

      return (image);
    }
}
