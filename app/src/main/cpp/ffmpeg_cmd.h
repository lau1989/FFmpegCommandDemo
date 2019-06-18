#include <jni.h>

#ifndef FFMPEGCOMMANDDEMO_FFMPEG_CMD_H
#define FFMPEGCOMMANDDEMO_FFMPEG_CMD_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_lau_ffmpegcommanddemo_MainActivity_ffmpegExec(JNIEnv *, jclass, jint, jobjectArray);

JNIEXPORT void JNICALL Java_com_lau_ffmpegcommanddemo_MainActivity_ffmpegExit(JNIEnv *, jclass);


#ifdef __cplusplus
}
#endif


#endif //FFMPEGCOMMANDDEMO_FFMPEG_CMD_H

void ffmpeg_progress(float progres);