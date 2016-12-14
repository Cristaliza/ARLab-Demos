//
//  ViewController.m
//  HelloARLib
//
//  Copyright (c) 2012 ARLab. All rights reserved.
//

#import "ViewController.h"

@implementation ViewController

@synthesize locationManager = _locationManager;
@synthesize locationDelegate;

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Hide StatusBar
    
    [[UIApplication sharedApplication] setStatusBarHidden:YES withAnimation:UIStatusBarAnimationSlide];
    
    // Init ARBrowser viewController //@"wTquyEUSqCo4oN+aOgMQy50EUw17arJlvixzfvxfcTA="
    
    _arlibCtrl = [[ARglLibController alloc] initWithAppKey:@"wTquyEUSqCo4oN+aOgMQy50EUw17arJlvixzfvxfcTA=" useDefaultCamera:YES];
    
  //  [_arlibCtrl.view setFrame:CGRectMake(0, 0, 320, 480)];
    
    /*****************************/
    /*** CUSTOM FRAME SAMPLE *****/
    /*****************************/
    
    // Set a custom frame for the ARBrowser view
    
    //[_arlibCtrl.view setFrame:CGRectMake( 20, 20, [UIScreen mainScreen].bounds.size.width - 40, [UIScreen mainScreen].bounds.size.width - 40)];
    
    [_arlibCtrl setPopupMode:popup_mode_bottom];
    [_arlibCtrl setFaceBookKey:@"263198400416633"];
    
    /*******************************************************************************************************************************
     Create several Pois:
     - POI location (lat and lon)
     - POI info (title and description) 
     - POI logo (image used in the label popup or the bottom popup)
     - POI actions
     *******************************************************************************************************************************/
    
    
    //////////////////////////////////
    ////////// FIRST POI /////////////
    //////////////////////////////////
    
    
    
    //init the poi	
    Poi* firstPoi = [[Poi alloc] init];
    //set the location of the poi with latitude and longtitude
    //[firstPoi setLocation:[[CLLocation alloc]initWithLatitude:32.781556 longitude:34.965985]];
    [firstPoi setLocation:[[CLLocation alloc]initWithLatitude:43.5248168 longitude:-5.6215671]];
    //set the altitude of the poi (variable between 45 degrees and -45)
//    [firstPoi setAltitude:40];
    //set the title of the poi
    [firstPoi setTitle:@"first poi"];
    //set the descripton of the poi
    [firstPoi setDescription:@"description of first poi"];
    //set the icon of the poi
    [firstPoi setIconPath:[UIImage imageNamed:@"target.png"]];
    //creat actions for the poi. the actions are made of a dictionary with the actions
    NSMutableDictionary* actions = [[NSMutableDictionary alloc] initWithObjectsAndKeys:@"1111111",POI_ACTION_CALL,@"22222222",POI_ACTION_SMS,@"http://google.com",POI_ACTION_WEBLINK, nil];
    //set the dictionary to the poi
    [firstPoi setActionsDict:actions];
    //add the poi to the arbrowser
    [_arlibCtrl add:firstPoi];
    
    //////////////////////////////////
    ////////// SECOND POI ////////////
    //////////////////////////////////
    
    Poi* secondPoi = [[Poi alloc] init];	
    [secondPoi setLocation:[[CLLocation alloc]initWithLatitude:32.801308 longitude:34.951158]];
    [secondPoi setTitle:@"second poi"];
    [secondPoi setDescription:@"description of second poi"];
    [secondPoi setIconPath:[UIImage imageNamed:@"cloud.png"]];
    actions = [[NSMutableDictionary alloc] initWithObjectsAndKeys:@"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",POI_ACTION_VIDEO,[NSString stringWithFormat:@"%f,%f",firstPoi.location.coordinate.latitude,firstPoi.location.coordinate.longitude],POI_ACTION_MAP,@"tweet this!",POI_ACTION_TWITTER, nil];
    [secondPoi setActionsDict:actions];
    [_arlibCtrl add:secondPoi];
    
    
    //////////////////////////////////
    ////////// THIRD POI /////////////
    //////////////////////////////////
    
    Poi* thirdPoi = [[Poi alloc] init];	
    [thirdPoi setLocation:[[CLLocation alloc]initWithLatitude:32.7955 longitude:34.968882]];
    [thirdPoi setAltitude:-5];
    [thirdPoi setSelectedAlpha:0.5];
    [thirdPoi setNotSelectedAlpha:0.1];
    [thirdPoi setTitle:@"third poi"];
    [thirdPoi setDescription:@"description of third poi"];
    [thirdPoi setIconPath:[UIImage imageNamed:@"Smiley.png"]];
    actions = [[NSMutableDictionary alloc] initWithObjectsAndKeys:@"http://www.netstate.com/states/symb/trees/images/tulip_tree.jpg",POI_ACTION_PHOTO,@"facebook this",POI_ACTION_FACEBOOK,@"http://www.samisite.com/sound/cropShadesofGrayMonkees.mp3",POI_ACTION_AUDIO,@"mail",POI_ACTION_EMAIL, nil];
    [thirdPoi setActionsDict:actions];
    [_arlibCtrl add:thirdPoi];
    
    [_arlibCtrl setPoiSize:poi_size_big];
    
    [_arlibCtrl start];
    [_arlibCtrl setEnableLocationAltitude:NO];
    
    [_arlibCtrl setDelegate:self];
    // Init Location Manager
    
    _locationManager = [[CLLocationManager alloc] init];
    _locationManager.delegate = self;
    
    [_locationManager startUpdatingLocation];
}
-(void) poiClicked:(Poi*)poi
{
    NSLog(@"clicked");
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.view addSubview:_arlibCtrl.view];
}

- (void)viewWillDisappear:(BOOL)animated
{
    
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    /*****************************/
    /******* IMPORTANT !!! *******/
    /*****************************/
    // Lock rotation.
    
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


#pragma mark location

- (void)locationManager:(CLLocationManager *)manager   didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    //CLLocation* loc = [[CLLocation alloc] initWithLatitude:32.787996 longitude:34.959526];
    [_arlibCtrl updateLocation:newLocation];
}

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error
{
    NSLog(@"error location:%@",error);
}



@end
