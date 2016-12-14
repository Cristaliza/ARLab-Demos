#import <CoreMedia/CoreMedia.h>
#import <AVFoundation/AVFoundation.h>
#import <UIKit/UIKit.h>

@protocol CameraCaptureDelegate
- (void)processNewCameraFrameYUV:(CVImageBufferRef)cameraFrame;
- (void)processNewCameraFrameRGB:(CVImageBufferRef)cameraFrame;
@end

__attribute__((__visibility__("default"))) @interface CaptureSessionManager : NSObject <AVCaptureVideoDataOutputSampleBufferDelegate> {
    
    
}

@property (strong,nonatomic) AVCaptureVideoPreviewLayer *previewLayer;
@property (strong,nonatomic) AVCaptureSession *captureSession;
@property (strong,nonatomic) AVCaptureStillImageOutput *stillImageOutput;
@property (strong,nonatomic) UIImage *stillImage;
@property (strong,nonatomic) NSNumber *outPutSetting;
@property (weak, nonatomic) id<CameraCaptureDelegate> delegate;

- (void)addVideoInput:(AVCaptureDevicePosition)_campos;
- (void)addVideoOutput;
- (void)addVideoPreviewLayer;

- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration;
+ (UIInterfaceOrientation)getInterfaceOrientationFromDeviceOrientation:(UIDeviceOrientation)dOrientation;

@end