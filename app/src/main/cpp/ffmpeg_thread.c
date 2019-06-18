#include "libavcodec/avcodec.h"
#include "ffmpeg_thread.h"
#include "android_log.h"

pthread_t ntid;
char **argvs = NULL;
int num = 0;


void *thread(void *arg) {
    LOGD("thread start run, argv num: %d", num);
    int result = ffmpeg_exec(num, argvs);
    ffmpeg_thread_exit(result);
    return ((void *) 0);
}

int ffmpeg_thread_run_cmd(int cmdnum, char **argv) {
    num = cmdnum;
    argvs = argv;

    int temp = pthread_create(&ntid, NULL, thread, NULL);
    if (temp != 0) {
        LOGE("can't create thread: %s ", strerror(temp));
        return 1;
    }
    LOGD("create thread success");
    return 0;
}

static void (*ffmpeg_callback)(int ret);

void ffmpeg_thread_callback(void (*cb)(int ret)) {
    ffmpeg_callback = cb;
}


void ffmpeg_thread_exit(int ret) {
    if (ffmpeg_callback) {
        ffmpeg_callback(ret);
    }
}


void ffmpeg_thread_cancel() {
    void *ret = NULL;
    pthread_join(ntid, &ret);
}