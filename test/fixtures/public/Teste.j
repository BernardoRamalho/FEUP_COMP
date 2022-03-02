.class public HelloWorld
.super java/lang/Object

.method public <init>()V
        .limit locals 1
        .limit stack 1
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
        .limit locals 5
        .limit stack 2
        iconst_5
        istore_1
        bipush 6
        istore_2
        iload_2
        bipush 10
        imul
        istore_3
        iload_1
        iload_3
        iadd
        istore_4
        iload_4
        invokestatic ioPlus.printResult(I)V
        return
.end method