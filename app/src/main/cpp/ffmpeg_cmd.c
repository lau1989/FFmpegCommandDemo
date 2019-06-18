//
// Created by lau on 2019-06-15.
//

#include "ffmpeg_cmd.h"

#include <jni.h>
#include <string.h>
#include "ffmpeg_thread.h"
#include "cmdutils.h"
#include "android_log.h"

static JavaVM *jvm = NULL;

static jobject m_clazz = NULL;

void callJavaMethod(JNIEnv *env, jclass clazz, int ret) {
    if (clazz == NULL) {
//        LOGE("--------------------clazz isNULL-----------------------");
        return;
    }

    jmethodID methodId = (*env)->GetStaticMethodID(env, clazz, "onExecuted", "(I)V");

    if (methodId == NULL) {
//        LOGE("------------------methodID isNull---------------------");
        return;
    }

    (*env)->CallStaticVoidMethod(env, clazz, methodId, ret);
}

//void callJavaMethodProgress(JNIEnv *env, jclass clazz, float ret) {
//    if (clazz == NULL) {
////        LOGE("-------------clazz isNULL---------------");
//        return;
//    }
//    jmethodID methodId = (*env)->GetStaticMethodID(env, clazz, "onProgress", "(F)V");
//    if (methodId == NULL) {
////        LOGE("-------------methodID isNULL---------------");
//        return;
//    }
//    (*env)->CallStaticVoidMethod(env, clazz, methodId, ret);
//}


static void ffmpeg_callback(int ret) {
    JNIEnv *env;
    (*jvm)->AttachCurrentThread(jvm, (void **) &env, NULL);
    callJavaMethod(env, m_clazz, ret);

    (*jvm)->DetachCurrentThread(jvm);
}

void ffmpeg_progress(float progress) {
    JNIEnv *env;
    (*jvm)->AttachCurrentThread(jvm, (void **) &env, NULL);

    (*jvm)->DetachCurrentThread(jvm);

}

JNIEXPORT jint JNICALL
Java_com_lau_ffmpegcommanddemo_MainActivity_ffmpegExec(JNIEnv *env, jclass clazz, jint cmdnum, jobjectArray cmdline) {
    (*env)->GetJavaVM(env, &jvm);
    m_clazz = (*env)->NewGlobalRef(env, clazz);

    int i = 0;
    char **argv = NULL;
    jstring *strr = NULL;
    if (cmdline != NULL) {
        argv = (char **) malloc(sizeof(char *) * cmdnum);
        strr = (jstring *) malloc(sizeof(jstring) * cmdnum);

        for (int i = 0; i < cmdnum; ++i) {
            strr[i] = (jstring) (*env)->GetObjectArrayElement(env, cmdline, i);
            argv[i] = (char *) (*env)->GetStringUTFChars(env, strr[i], 0);
        }
    }

    LOGD("exec command: %d", cmdnum);
    ffmpeg_thread_run_cmd(cmdnum, argv);

    ffmpeg_thread_callback(ffmpeg_callback);

    free(strr);
    return 0;

}

JNIEXPORT void JNICALL
Java_com_lau_ffmpegcommanddemo_MainActivity_ffmpegExit(JNIEnv *env, jclass type) {

}