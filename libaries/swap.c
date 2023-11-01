#include <windows.h>
#include <stdio.h>
#include <string.h>
#include "src_call.h"

JNIEXPORT void JNICALL Java_src_call_wallpaperSwap(JNIEnv * a, jclass b, jstring c){
    jboolean copy = FALSE;
    jchar* c_str = (*a)-> GetStringChars(a,c,&copy);

    SystemParametersInfoW(SPI_SETDESKWALLPAPER,0,c_str,SPIF_SENDCHANGE);
    // MessageBox(NULL,"hello","bye",MB_OK);
    MessageBoxW(NULL, c_str, NULL, 0);
   (*a)-> ReleaseStringChars(a,c,c_str);
}











