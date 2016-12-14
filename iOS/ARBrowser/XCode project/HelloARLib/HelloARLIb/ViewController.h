//
//  ViewController.h
//  HelloARLIb
//
//  Copyright (c) 2012 ARLab. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <EADBrowser/ARglLibController.h>
//#import "ARglLibController.h"

@interface ViewController : UIViewController <CLLocationManagerDelegate,ARLibProtocol>
{
    ARglLibController* _arlibCtrl;
    __weak id locationDelegate;
    CLLocationManager *_locationManager;
}

@property (nonatomic, strong) CLLocationManager *locationManager;  
@property (weak) id locationDelegate;



@end
