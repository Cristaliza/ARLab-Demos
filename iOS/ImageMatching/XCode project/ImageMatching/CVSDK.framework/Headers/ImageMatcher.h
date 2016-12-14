//
//  ImageMatcher.h
//  CVSDK IMM
//
//  Copyright (c) 2012 ARLab. All rights reserved.
//
//  Version 1.20
//

/**
 * Matcher framework
 * The matcher framework analyzes the images feeded from the camera or any other source
 * and will look in the image for one of the images in the images pool.
 * When an image is found its id is return.
 * 
 * The matcher can simutanisly look for QR and bar code in the image feed.
 */

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import <CoreGraphics/CoreGraphics.h>
#import <CoreVideo/CoreVideo.h>
#import <CoreMedia/CoreMedia.h>
#import "CaptureSessionManager.h"
//#import "ARQrCodeLib.h"

#ifndef CVSDK
#define CVSDK

#import "_ARLab.h"

typedef enum  {
    matcher_mode_Image,
    matcher_mode_QRcode,
    matcher_mode_All
}matcher_mode;

@protocol matcherProtocol <NSObject>

/**
 *  Optional callback: Called when an Image in the images pool is matched.
 *  @param uId: The matched image's unique ID
 */

-(void)imageRecognitionResult:(int)uId ARLAB_AVAILABLE(IMM_120401);

@end

@protocol matcherQRProtocol <NSObject>

@optional

/**
 *  Optional callback: Called when a QR or BAR code found
 *  @param code: The string of the code
 */
-(void)singleQRrecognitionResult:(NSString*)code ARLAB_AVAILABLE(IMM_120501);

/**
 *  Optional callback: Called when a more than one QR or BAR codes found
 *  @param codes: Array of ROIs found each ROI has the CGRect defining the crop rectangle and the QR string representation
 */
-(void)multipleQRrecognitionResult:(NSArray*)codes ARLAB_AVAILABLE(IMM_120501);

@end

__attribute__((__visibility__("default"))) @interface ImageMatcher : UIViewController <CameraCaptureDelegate> {
@protected
    NSString* _appKey3DR ARLAB_AVAILABLE(IMM_120901);
}

/**
 *  @brief Add image to the image pool.
 *
 *  @param image: The image to be added to the pool.
 *
 *  @return The image's unique id, -1 if image doesnt have enough keypoints.
 */
- (NSNumber*) addImage:(UIImage*)image ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Add image from the internet to the image pool.
 *
 *  @param imageURl: Image url of the image to be added (e.g. http://www.url.com/image.jpg)
 *
 *  @return The image's unique id, -1 if image from web doesnt have enough keypoints or coudn't be downloaded.
 */
- (NSNumber*) addImageFromUrl:(NSURL*)imageUrl ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Add image with unique id.
 *
 *  @param image: The image to be added
 *  @param uId: The unique id to be associated with the image.
 *
 *  @return The image's unique id, -1 if image doesnt have enough keypoints.
 */
- (BOOL) addImage:(UIImage*)image withUniqeID:(NSNumber*)uId ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Add image from the internet with unique id.
 *
 *  @param imageURl: Image url of the image to be added (e.g. http://www.url.com/image.jpg).
 *  @param uId: The unique id to be associated with the image.
 *
 *  @return The image's unique id, -1 if image from web doesnt have enough keypoints or coudn't be downloaded.
 */
- (BOOL) addImageFromUrl:(NSURL*)imageUrl withUniqeID:(NSNumber*)uId ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Add image data from to the image pool.
 *
 *  @param data: data of the image to be matched
 *
 *  @return The image's unique id, -1 if data is invalid
 */
- (NSNumber*) addImageFromData:(NSData*)data ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Add image data to pool with unique id.
 *
 *  @param data: data of the image to be matched
 *  @param uId: The unique id to be associated with the image.
 *
 *  @return False if data is invalid
 */
- (BOOL) addImageFromData:(NSData*)data withUniqeID:(NSNumber*)uId ARLAB_AVAILABLE(IMM_120503);

/**
 *  @brief Add image data from the to the image pool.
 *
 *  @param url: url with data of the image to be matched
 *
 *  @return The image's unique id, -1 if data is invalid
 */
- (NSNumber*) addImageFromDataThroughUrl:(NSURL*)url ARLAB_AVAILABLE(IMM_120503);

/**
 *  @brief Add image data to the pool with unique id.
 *
 *  @param url: url with  data of the image to be matched
 *  @param uId: The unique id to be associated with the image.
 *
 *  @return False if data is invalid
 */
- (BOOL) addImageFromDataThroughUrl:(NSURL*)url withUniqeID:(NSNumber*)uId ARLAB_AVAILABLE(IMM_120503);

/**
 *  @brief Remove image from the images pool.
 *
 *  @param uId: The unique id associated with the image to be deleted.
 *
 *  @return True if image is found and removed.
 */
- (BOOL) removeImage:(NSNumber*)uId ARLAB_AVAILABLE(IMM_120401);

/**
 * @brief   Get quality Rating of the input Image
 *
 * @param   Input image to be analyzed
 *
 * @return  Quality Rating
 */
- (NSNumber*)getImageRating:(UIImage*)image ARLAB_AVAILABLE(IMM_120803);

/**
 *  @brief Remove all the image in the images pool.
 */
- (void) removeAllImages ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Start the phone camera and start the matcher engine.
 */
- (void) start ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Stop the phone camera and stop the matcher engine.
 */
- (void) stop ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Process an image and searches for an image or QR code in it.
 *
 *  @param image to be processed
 */
- (void) processUIImage:(UIImage* )image ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Process an image and searches for an image or QR code in it.
 *  Camera output settings should be either kCVPixelFormatType_420YpCbCr8BiPlanarFullRange or kCVPixelFormatType_420YpCbCr8BiPlanarVideoRange
 *  @param image to be processed
 */
- (void)processNewCameraFrameYUV:(CVImageBufferRef)imageBuffer ARLAB_AVAILABLE(IMM_120803);

/**
 *  @brief Process an image and searches for an image or QR code in it.
 *  Camera output settings should be kCVPixelFormatType_32BGRA
 *  @param image to be processed
 */
- (void)processNewCameraFrameRGB:(CVImageBufferRef)imageBuffer ARLAB_AVAILABLE(IMM_120803);

/**
 *  @brief Set matcher mode
 *  @param mode
 *  mode can be any of the following:
 *  matcher_mode_Image: Image only matching.
 *  matcher_mode_QRcode: QR code matching.
 *  matcher_mode_All: Both image and QR code matching (default mode).
 *
 */
- (void) setMatchMode:(matcher_mode)mode ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Each image added to the image pool for matching is analized and given a rating
 *  indicating how 'hard' is it to be matched. This rating is from 0-10 and can be set
 *  in this method.
 *
 *  @param rating [0..10]: 0 all images pass to the pool, 10 only the most feature-full images do
 *  default value is 5
 */
- (void) setImagePoolMinimumRating:(int)rating ARLAB_AVAILABLE(IMM_120401);


-(id) init;

/**
 *  @brief Init with your arLab app key.
 *
 *  @param appKey
 */
- (id) initWithAppKey:(NSString*) appKey ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Init with your arLab app key.
 *
 *  @param appKey: The app key obtained from ARlab site.
 *  @param useDefaultCamera: Use default camera flag.
 *  @param appKey3DR is "" in this case.
 */
-(id) initWithAppKey:(NSString*) appKey useDefaultCamera:(BOOL)useDefaultCamera ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Init with your arLab app key.
 *
 *  @param appKey: The app key obtained from ARlab site.
 *  @param useDefaultCamera: Use default camera flag.
 *  @param appKey3DR: The app key for the 3DR Engine view.
 *
 *  @discussion For 3DR Lite version, the one that comes with IMT engine, the appKey is the same as the Image Tracker one.
 *  For 3DR Advanced version, the appKey is the obtained from ARLab site.
 *  If do not want to init the GLView, set this param to @"".
 */
-(id) initWithAppKey:(NSString*) appKey useDefaultCamera:(BOOL)useDefaultCamera with3DappKey:(NSString*)appKey3DR ARLAB_AVAILABLE(IMM_120803);

/**
 *  @brief Add ROI (Region Of Intrest).
 *
 *  @param roi: the Roi to be added. ROI has the CGRect defining the crop rectangle and the QR string representation.
 *
 *  @return returns false if ROI's rect doesn't fit the image or it crosses other ROI.
 */

//- (BOOL) addQrRoi:(Roi*) roi ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Remove al ROIs
 */
//- (void) clearQrRois ARLAB_AVAILABLE(IMM_120401);

/**
 *  @brief Set MinKeyPoints Value
 *  This value represents the minum amount of keypoints accepted to add an image to the pool.
 *  Default value is 50.
 *  @discussion When this value is higher, more information is requested from the image to be added.
 */
- (void) setMinKeyPointsValue: (NSNumber*)value ARLAB_AVAILABLE(IMM_120803);

/**
 *  @brief set MaxKeyPoints Value
 *  This value represents the maximum amount of keypoits accepted when matching.
 *  Default value is 50.
 *  @discussion When this value is higher the matching quality grows but the performance (speed) decreases.
 */
- (void) setMaxKeyPointsValue: (NSNumber*)value ARLAB_AVAILABLE(IMM_120803);

/**
 *  @brief Get MinKeyPoints Value
 *  This value represents the minum amount of keypoints accepted to add an image to the pool.
 *  Default value is 50.
 *  @discussion When this value is higher, more information is requested from the image to be added.
 */
- (NSNumber*)getMinKeyPointsValue ARLAB_AVAILABLE(IMM_120803);

/**
 *  Get MaxKeyPoints Value
 *  This value represents the maximum amount of keypoits accepted when matching.
 *  Default value is 50.
 *  @discussion When this value is higher the matching quality grows but the performance (speed) decreases.
 */
- (NSNumber*)getMaxKeyPointsValue ARLAB_AVAILABLE(IMM_120803);


/**
 *  Crop rectangle: The framework will look for images in the crop rectangle.
 */
@property (assign, nonatomic) CGRect ImageCropRect ARLAB_DEPRECATED(IMM_120401, IMM_120803);

/**
 *  Callback delegate.
 */
@property (weak, nonatomic) id <matcherProtocol> matcherDelegate;
/**
 *  Callback delegate.
 */
//@property (weak, nonatomic) id <matcherQRProtocol> matcherQRDelegate;

/**
 *  When set to true enables a median filter for the image matcher.
 *  The filter helps remove "noise" from the search algorithem.
 *  Default is False.
 */
@property (assign, nonatomic) BOOL enableMedianFilter;

/**
 *  Device Camera Position
 */
@property (assign, nonatomic) AVCaptureDevicePosition camPos;


@end

#endif