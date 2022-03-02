.class public QuickSort
.super java/lang/Object
    .method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method
.method public static main([Ljava/lang/String;)V
.limit locals 7
.limit stack 2
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
new QuickSort
astore 6
aload 6
invokespecial QuickSort.<init>()V
aload 6
aload_1
invokevirtual QuickSort.quicksort([I)Z
pop
aload 6
aload_1
invokevirtual QuickSort.printL([I)Z
pop
return
.end method
.method public printL([I)Z
.limit locals 5
.limit stack 2
iconst_0
istore_2
Loop3:
aload_1
arraylength
istore_3
iload_2
iload_3
if_icmpge Body3
goto EndLoop3
Body3:
aload_1
iload_2
iaload
istore 4
iload 4
invokestatic io.println(I)V
iload_2
iconst_1
iadd
istore_2
EndLoop3:
iconst_1
ireturn
.end method
.method public quicksort([I)Z
.limit locals 5
.limit stack 4
aload_1
arraylength
istore_2
iload_2
iconst_1
isub
istore_3
aload_0
aload_1
iconst_0
iload_3
invokevirtual QuickSort.quicksort([III)Z
istore 4
iload 4
ireturn
.end method
.method public quicksort([III)Z
.limit locals 7
.limit stack 4
iload_2
iload_3
if_icmpge else1
aload_0
aload_1
iload_2
iload_3
invokevirtual QuickSort.partition([III)I
istore 4
iload 4
iconst_1
isub
istore 5
aload_0
aload_1
iload_2
iload 5
invokevirtual QuickSort.quicksort([III)Z
pop
iload 4
iconst_1
iadd
istore 6
aload_0
aload_1
iload 6
iload_3
invokevirtual QuickSort.quicksort([III)Z
pop
goto endif1
else1:
endif1:
iconst_1
ireturn
.end method
.method public partition([III)I
.limit locals 9
.limit stack 4
aload_1
iload_3
iaload
istore 4
iload_2
istore 5
iload_2
istore 6
Loop4:
iload 6
iload_3
if_icmpge Body4
Body4:
aload_1
iload 6
iaload
istore 7
iload 7
iload 4
if_icmpge else2
aload_1
iload 5
iaload
istore 8
aload_1
iload 5
aload_1
iload 6
iaload
iastore
aload_1
iload 6
iload 8
iastore
iload 5
iconst_1
iadd
istore 5
goto endif2
else2:
endif2:
iload 6
iconst_1
iadd
istore 6
EndLoop4:
aload_1
iload 5
iaload
istore 8
aload_1
iload 5
aload_1
iload_3
iaload
iastore
aload_1
iload_3
iload 8
iastore
iload 5
ireturn
.end method