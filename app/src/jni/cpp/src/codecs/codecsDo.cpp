#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-parameter"
#pragma ide diagnostic ignored "hicpp-signed-bitwise"
//
// Created by zhc-2 on 2019/6/19.
//

#include "./codecsDo.h"

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-pragmas"
#pragma ide diagnostic ignored "OCUnusedGlobalDeclarationInspection"

void Callback(JNIEnv *env, jobject callback, const char *str, double d) {
    JNIEnv e = *env;
    jclass cls = e.GetObjectClass(callback);
    jmethodID mid = e.GetMethodID(cls, "callback", "(Ljava/lang/String;D)V");
    jstring s = e.NewStringUTF(str);
    e.CallVoidMethod(callback, mid, s, (jdouble) d);
    e.DeleteLocalRef(cls);
    e.DeleteLocalRef(s);
}

JNIEXPORT jint JNICALL Java_pers_zhc_tools_jni_JNI_00024Codecs_qmcDecode
        (JNIEnv *env, jclass cls, jstring f, jstring dF, jint mode, jobject callback) {
    JNIEnv e = *env;
    const char *f1 = e.GetStringUTFChars(f, (jboolean *) 0);
    const char *f2 = e.GetStringUTFChars(dF, (jboolean *) 0);
    char *sQ = NULL;
    strcat_auto(&sQ, f1);
    strcat_auto(&sQ, " -> ");
    strcat_auto(&sQ, f2);
    Log(env, "", sQ);
    Log(env, "", "JNI————解码……");
    int rC;
    char u1[strlen(f1) + 1], u2[strlen(f2) + 1];
    ToUpperCase(u1, f1), ToUpperCase(u2, f2);
    char *nN = NULL;
    if (!strcmp(u1, u2)) NewFileName(&nN, f1), rC = decode(f1, nN, env, callback), remove(f1), rename(nN, f1);
    else
        rC = decode(f1, f2, env, callback);
    if (mode) remove(f1);
    return (jint) rC;
}

JNIEXPORT jint JNICALL Java_pers_zhc_tools_jni_JNI_00024Codecs_kwmDecode
        (JNIEnv *env, jclass cls, jstring f, jstring dF, jint mode, jobject callback) {
    JNIEnv e = *env;
    const char *fN = e.GetStringUTFChars(f, (jboolean *) 0);
    const char *dFN = e.GetStringUTFChars(dF, (jboolean *) 0);
    int stt;
    char u1[strlen(fN) + 1], u2[strlen(dFN) + 1];
    ToUpperCase(u1, fN), ToUpperCase(u2, dFN);
    char *nN = NULL;
    if (!strcmp(u1, u2)) NewFileName(&nN, fN), stt = kwm(env, fN, nN, callback), remove(fN), rename(nN, fN);
    else stt = kwm(env, fN, dFN, callback);
    if (mode) remove(fN);
    return stt;
}

JNIEXPORT jint JNICALL Java_pers_zhc_tools_jni_JNI_00024Codecs_Base128_1encode
        (JNIEnv *env, jclass cls, jstring f1, jstring f2, jint mode, jobject callback) {
    const char *FileName = (*env).GetStringUTFChars(f1, (jboolean *) NULL);
    const char *DestFileName = (*env).GetStringUTFChars(f2, (jboolean *) NULL);
    char upperCaseD1[strlen(FileName) + 1], upperCaseD2[strlen(DestFileName) + 1];
    ToUpperCase(upperCaseD1, FileName);
    ToUpperCase(upperCaseD2, DestFileName);
    char *nFN = NULL;
    if (!strcmp(upperCaseD1, upperCaseD2)) {
        NewFileName(&nFN, FileName);
        int status = eD(FileName, nFN, env, callback);
        if (status != 0) return status;
        remove(FileName);
        rename(nFN, DestFileName);
    } else {
        eD(FileName, DestFileName, env, callback);
    }
    if (mode) remove(FileName);
    return 0;
}

JNIEXPORT jint JNICALL Java_pers_zhc_tools_jni_JNI_00024Codecs_Base128_1decode
        (JNIEnv *env, jclass cls, jstring f1, jstring f2, jint mode, jobject callback) {
    const char *FileName = (*env).GetStringUTFChars(f1, (jboolean *) NULL);
    const char *DestFileName = (*env).GetStringUTFChars(f2, (jboolean *) NULL);
    char upperCaseD1[strlen(FileName) + 1], upperCaseD2[strlen(DestFileName) + 1];
    ToUpperCase(upperCaseD1, FileName);
    ToUpperCase(upperCaseD2, DestFileName);
    char *nFN = NULL;
    if (!strcmp(upperCaseD1, upperCaseD2)) {
        NewFileName(&nFN, FileName);
        int status = dD(FileName, nFN, env, callback);
        if (status != 0) return status;
        remove(FileName);
        rename(nFN, DestFileName);
    } else {
        dD(FileName, DestFileName, env, callback);
    }
    if (mode) remove(FileName);
    return 0;
}

#pragma clang diagnostic pop
#pragma clang diagnostic pop