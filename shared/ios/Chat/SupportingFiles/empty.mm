#import <Foundation/Foundation.h>

/*
 This file exists only to force Xcode to use the GNU c++ standard library (libstdc++).
 Even if the project settings indicate they want to use libstdc++, if there is no .mm file, Xcode
 continues to use its c++ lib. That prevents the app from linking if it includes some .hh files.

 @see: http://stackoverflow.com/a/19250215/3936576

 */
