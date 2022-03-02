.class public Lazysort
.super java/lang/Object
    .method public <init>()V
aload_0
invokespecial Quicksort/<init>()V
return
.end method
.method public static main([Ljava/lang/String;)V
.limit locals 9
.limit stack 4
bipush 10
newarray int
astore_1
iconst_0
istore_3
Loop2:
aload_1
arraylength
istore 4
iload_3
iload 4
if_icmpge Body2
goto EndLoop2
Body2:
aload_1
arraylength
istore 5
aload_1
iload_3
iload 5
iload_3
isub
iastore
iload_3
iconst_1
iadd
istore_3
EndLoop2:
new Lazysort
astore 6
aload 6
invokespecial Lazysort.<init>()V
aload 6
aload_1
invokevirtual Lazysort.quicksort([I)Z
pop
aload 6
aload_1
invokevirtual Lazysort.printL([I)Z
istore 8
return
.end method
.method public quicksort([I)Z
.limit locals 9
.limit stack 7
iconst_0
iconst_5
invokestatic MathUtils.random(II)I
istore_2
iload_2
iconst_4
if_icmpge else1
aload_0
aload_1
invokevirtual Lazysort.beLazy([I)Z
pop
iconst_1
istore 4
goto endif1
else1:
iconst_0
istore 4
endif1:
iload 4
iconst_1
iload 4
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 4
goto endif2
else2:
aload_1
arraylength
istore 7
iload 7
iconst_1
isub
istore 8
aload_0
aload_1
iconst_0
iload 8
invokevirtual Lazysort.quicksort([III)Z
istore 4
endif2:
iload 4
ireturn
.end method
.method public beLazy([I)Z
.limit locals 7
.limit stack 4
aload_1
arraylength
istore_2
iconst_0
istore_3
Loop3:
iload_2
iconst_2
idiv
istore 4
iload_3
iload 4
if_icmpge Body3
goto EndLoop3
Body3:
aload_1
iload_3
iconst_0
bipush 10
invokestatic MathUtils.random(II)I
iastore
iload_3
iconst_1
iadd
istore_3
EndLoop3:
Loop4:
iload_3
iload_2
if_icmpge Body4
Body4:
iconst_0
bipush 10
invokestatic MathUtils.random(II)I
istore 6
aload_1
iload_3
iload 6
iconst_1
iadd
iastore
iload_3
iconst_1
iadd
istore_3
EndLoop4:
iconst_1
ireturn
.end method



