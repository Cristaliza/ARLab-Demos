//
//  ViewController.h
//  ImageMatching
//
//  Created by Cristaliza on 12/2/16.
//  Copyright (c) 2015 Cristaliza S.L. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CVSDK/ImageMatcher.h>

@interface ViewController : UIViewController <matcherProtocol> {
    
    ImageMatcher* _cvView;
    NSTimer *timerTitle;
    int IDImage;
    NSMutableArray *IdsImages;
    NSMutableArray *ArrayTitlesImages;
    UIAlertView *alert;
}

@property (nonatomic,retain) IBOutlet UIView *viwCamara;

/**
 *
 *Add images into the library (in background).
 *
 */
-(void)addImagesInLibrary;

/**
 *
 *Add an image (which is into the project) into the library.
 *
 *@param [NSString]img Image name.
 *
 *@param [NSString]titleImage Title that the image has got associated.
 *
 */
-(void)addImageToPool:(NSString*)img withTitle:(NSString*)titleImage;

/**
 *
 *Add an image (from an URL) into the library.
 *
 *@param [NSString]img Image url.
 *
 *@param [NSString]titleImage Title that the image has got associated.
 *
 */
-(void)addImageToPoolWithURL:(NSString*)imgPath withTitle:(NSString*)titleImage;

/**
 *
 *Add an image (which is into the project) into the library.
 *
 *@param [NSString]img Image name.
 *
 *@param [NSNumber]uId Image Id.
 *
 *@param [NSString]titleImage Title that the image has got associated.
 *
 */
-(void)addImageToPool:(NSString*)img withUniqueID:(NSNumber*)uId withTitle:(NSString*)titleImage;


@end

