//
//  ViewController.m
//  ImageMatching
//
//  Created by Cristaliza on 12/2/16.
//  Copyright (c) 2015 Cristaliza S.L. All rights reserved.
//
#import "ViewController.h"

//#import <CVSDK/Test.h>


@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    IdsImages = [[NSMutableArray alloc] init];
    ArrayTitlesImages = [[NSMutableArray alloc] init];
    
    // Set frame
    [[UIApplication sharedApplication] setStatusBarHidden:YES withAnimation:UIStatusBarAnimationSlide];
    
    _cvView = [[ImageMatcher alloc] initWithAppKey:@"svm5zQ0XG+7EPOSwlOSN9UWdKxhjcpHUy3XHKpLrdM8="
useDefaultCamera:TRUE];
    
    // Register delegate
    _cvView.matcherDelegate = self;
    
    // Start
    [_cvView start];
    
    // Set filter
    [_cvView setEnableMedianFilter:YES];
    
    // Set image minimum rating required to enter in the pool
    [_cvView setImagePoolMinimumRating:1];
    
    // Set match mode
    [_cvView setMatchMode:matcher_mode_Image];

    [self.view insertSubview:_cvView.view belowSubview:self.view];

    //Add images into the library
    alert = [[UIAlertView alloc] initWithTitle:@"Loading" message:nil delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
    UIActivityIndicatorView *spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    spinner.center = CGPointMake(142,70);
    [spinner startAnimating];
    [alert addSubview:spinner];
    [alert show];
       
    [self performSelectorInBackground:@selector(addImagesInLibrary) withObject:nil];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)imageRecognitionResult:(int)uId
{
    if(uId != -1){
        NSLog(@"***MATCH - Image id:*** %d", uId);
        IDImage = uId;
        
        NSString *titleString;
        for(int i=0; i<[IdsImages count]; i++){
            if(IDImage == [[IdsImages objectAtIndex:i]intValue]){
                titleString = [ArrayTitlesImages objectAtIndex:i];
                break;
            }
        }
        
        [alert setTitle:titleString];
        [alert show];
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [alert dismissWithClickedButtonIndex:alert.cancelButtonIndex animated:YES];
        });

    }
}


-(void)addImagesInLibrary{
    // Add images to the pool
    //[self addImageToPool:@"pic2.jpeg" withUniqueID:[NSNumber numberWithInt:112] withTitle:@"pic2"];
    //[self addImageToPoolFromData:@"ARLab@pic2jpegdetector_data" withTitle:@"pic2"];
    
    // You can also use your own ids
    [self addImageToPool:@"pic6.jpeg" withTitle:@"pic6"];
    [self addImageToPool:@"pic7.jpeg" withTitle:@"pic7"];
    [self addImageToPool:@"pic8.jpeg" withTitle:@"pic8"];
    [self addImageToPool:@"pic9.jpeg" withTitle:@"pic9"];
    
   
    
    
    [self performSelectorOnMainThread:@selector(removeAlertWithSpinner) withObject:nil waitUntilDone:YES];
}


-(void)removeAlertWithSpinner{
    
    [alert dismissWithClickedButtonIndex:0 animated:YES];
}

-(void)addImageToPool:(NSString*)img withTitle:(NSString*)titleImage{
    
    NSNumber* res = [_cvView addImage:[UIImage imageNamed:img]];
    NSLog(@"*** Image named %@, with id %d %@ ***", img, [res intValue], [res intValue] != -1 ? @"ADDED":@"NOT ADDED");
    [IdsImages addObject:res];
    [ArrayTitlesImages addObject:titleImage];
}

-(void)addImageToPoolFromData:(NSString*)img withTitle:(NSString*)titleImage{
    
    NSNumber* res = [_cvView addImageFromData:[NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:img ofType:@"dat"]]];
    NSLog(@"*** Image named %@, with id %d %@ ***", img, [res intValue], [res intValue] != -1 ? @"ADDED":@"NOT ADDED");
    [IdsImages addObject:res];
    [ArrayTitlesImages addObject:titleImage];
}

-(void)addImageToPoolWithURL:(NSString*)imgPath withTitle:(NSString*)titleImage{
    
    NSNumber* res = [_cvView addImageFromUrl:[NSURL URLWithString:imgPath]];
    NSLog(@"*** Image named %@, with id %d %@ ***", imgPath, [res intValue], [res intValue] != -1 ? @"ADDED":@"NOT ADDED");
    [IdsImages addObject:res];
    [ArrayTitlesImages addObject:titleImage];
}

-(void)addImageToPool:(NSString*)img withUniqueID:(NSNumber*)uId withTitle:(NSString*)titleImage{
    
    BOOL res = [_cvView addImage:[UIImage imageNamed:img] withUniqeID:uId];
    NSLog(@"*** %@ ***", res ? @"ADDED" : @"NOT ADDED");
    [IdsImages addObject:uId];
    [ArrayTitlesImages addObject:titleImage];
    
}


@end
