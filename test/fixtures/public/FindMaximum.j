.class public Life
.super java/lang/Object
.field private 'UNDERPOP_LIM' I
.field private 'OVERPOP_LIM' I
.field private 'REPRODUCE_NUM' I
.field private 'LOOPS_PER_MS' I
.field private 'xMax' I
.field private 'yMax' I
.field private 'field' [I
    .method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method
.method public static main([Ljava/lang/String;)V
.limit locals 5
.limit stack 1
new Life
astore_1
aload_1
invokespecial Life.<init>()V
aload_1
invokevirtual Life.init()Z
pop
Loop2:
iconst_1
ifne Body2
Body2:
aload_1
invokevirtual Life.printField()Z
pop
aload_1
invokevirtual Life.update()Z
pop
invokestatic io.read()I
istore 4
EndLoop2:
return
.end method
.method public init()Z
.limit locals 15
.limit stack 2
iconst_1
newarray int
astore_1
iconst_2
istore_3
aload_0
iload_3
putfield Life/UNDERPOP_LIM I
iconst_3
istore 4
aload_0
iload 4
putfield Life/OVERPOP_LIM I
iconst_3
istore 5
aload_0
iload 5
putfield Life/REPRODUCE_NUM I
ldc 225000
istore 6
aload_0
iload 6
putfield Life/LOOPS_PER_MS I
aload_0
aload_1
invokevirtual Life.field([I)[I
astore 7
aload_0
aload 7
putfield Life/field [I
iconst_0
istore 8
aload_1
iload 8
iaload
istore 9
iload 9
iconst_1
isub
istore 10
aload_0
iload 10
putfield Life/xMax I
aload_0
getfield Life/field [I
astore 11
aload 11
arraylength
istore 12
iload 12
iload 9
idiv
istore 13
iload 13
iconst_1
isub
istore 14
aload_0
iload 14
putfield Life/yMax I
iconst_1
ireturn
.end method
.method public field([I)[I
.limit locals 206
.limit stack 3
bipush 100
newarray int
astore_2
aload_0
aload_2
putfield Life/field [I
iconst_0
istore 4
aload_1
iload 4
bipush 10
iastore
aload_0
getfield Life/field [I
astore 5
iconst_0
istore 6
aload 5
iload 6
iconst_0
iastore
aload_0
getfield Life/field [I
astore 7
iconst_1
istore 8
aload 7
iload 8
iconst_0
iastore
aload_0
getfield Life/field [I
astore 9
iconst_2
istore 10
aload 9
iload 10
iconst_1
iastore
aload_0
getfield Life/field [I
astore 11
iconst_3
istore 12
aload 11
iload 12
iconst_0
iastore
aload_0
getfield Life/field [I
astore 13
iconst_4
istore 14
aload 13
iload 14
iconst_0
iastore
aload_0
getfield Life/field [I
astore 15
iconst_5
istore 16
aload 15
iload 16
iconst_0
iastore
aload_0
getfield Life/field [I
astore 17
bipush 6
istore 18
aload 17
iload 18
iconst_0
iastore
aload_0
getfield Life/field [I
astore 19
bipush 7
istore 20
aload 19
iload 20
iconst_0
iastore
aload_0
getfield Life/field [I
astore 21
bipush 8
istore 22
aload 21
iload 22
iconst_0
iastore
aload_0
getfield Life/field [I
astore 23
bipush 9
istore 24
aload 23
iload 24
iconst_0
iastore
aload_0
getfield Life/field [I
astore 25
bipush 10
istore 26
aload 25
iload 26
iconst_1
iastore
aload_0
getfield Life/field [I
astore 27
bipush 11
istore 28
aload 27
iload 28
iconst_0
iastore
aload_0
getfield Life/field [I
astore 29
bipush 12
istore 30
aload 29
iload 30
iconst_1
iastore
aload_0
getfield Life/field [I
astore 31
bipush 13
istore 32
aload 31
iload 32
iconst_0
iastore
aload_0
getfield Life/field [I
astore 33
bipush 14
istore 34
aload 33
iload 34
iconst_0
iastore
aload_0
getfield Life/field [I
astore 35
bipush 15
istore 36
aload 35
iload 36
iconst_0
iastore
aload_0
getfield Life/field [I
astore 37
bipush 16
istore 38
aload 37
iload 38
iconst_0
iastore
aload_0
getfield Life/field [I
astore 39
bipush 17
istore 40
aload 39
iload 40
iconst_0
iastore
aload_0
getfield Life/field [I
astore 41
bipush 18
istore 42
aload 41
iload 42
iconst_0
iastore
aload_0
getfield Life/field [I
astore 43
bipush 19
istore 44
aload 43
iload 44
iconst_0
iastore
aload_0
getfield Life/field [I
astore 45
bipush 20
istore 46
aload 45
iload 46
iconst_0
iastore
aload_0
getfield Life/field [I
astore 47
bipush 21
istore 48
aload 47
iload 48
iconst_1
iastore
aload_0
getfield Life/field [I
astore 49
bipush 22
istore 50
aload 49
iload 50
iconst_1
iastore
aload_0
getfield Life/field [I
astore 51
bipush 23
istore 52
aload 51
iload 52
iconst_0
iastore
aload_0
getfield Life/field [I
astore 53
bipush 24
istore 54
aload 53
iload 54
iconst_0
iastore
aload_0
getfield Life/field [I
astore 55
bipush 25
istore 56
aload 55
iload 56
iconst_0
iastore
aload_0
getfield Life/field [I
astore 57
bipush 26
istore 58
aload 57
iload 58
iconst_0
iastore
aload_0
getfield Life/field [I
astore 59
bipush 27
istore 60
aload 59
iload 60
iconst_0
iastore
aload_0
getfield Life/field [I
astore 61
bipush 28
istore 62
aload 61
iload 62
iconst_0
iastore
aload_0
getfield Life/field [I
astore 63
bipush 29
istore 64
aload 63
iload 64
iconst_0
iastore
aload_0
getfield Life/field [I
astore 65
bipush 30
istore 66
aload 65
iload 66
iconst_0
iastore
aload_0
getfield Life/field [I
astore 67
bipush 31
istore 68
aload 67
iload 68
iconst_0
iastore
aload_0
getfield Life/field [I
astore 69
bipush 32
istore 70
aload 69
iload 70
iconst_0
iastore
aload_0
getfield Life/field [I
astore 71
bipush 33
istore 72
aload 71
iload 72
iconst_0
iastore
aload_0
getfield Life/field [I
astore 73
bipush 34
istore 74
aload 73
iload 74
iconst_0
iastore
aload_0
getfield Life/field [I
astore 75
bipush 35
istore 76
aload 75
iload 76
iconst_0
iastore
aload_0
getfield Life/field [I
astore 77
bipush 36
istore 78
aload 77
iload 78
iconst_0
iastore
aload_0
getfield Life/field [I
astore 79
bipush 37
istore 80
aload 79
iload 80
iconst_0
iastore
aload_0
getfield Life/field [I
astore 81
bipush 38
istore 82
aload 81
iload 82
iconst_0
iastore
aload_0
getfield Life/field [I
astore 83
bipush 39
istore 84
aload 83
iload 84
iconst_0
iastore
aload_0
getfield Life/field [I
astore 85
bipush 40
istore 86
aload 85
iload 86
iconst_0
iastore
aload_0
getfield Life/field [I
astore 87
bipush 41
istore 88
aload 87
iload 88
iconst_0
iastore
aload_0
getfield Life/field [I
astore 89
bipush 42
istore 90
aload 89
iload 90
iconst_0
iastore
aload_0
getfield Life/field [I
astore 91
bipush 43
istore 92
aload 91
iload 92
iconst_0
iastore
aload_0
getfield Life/field [I
astore 93
bipush 44
istore 94
aload 93
iload 94
iconst_0
iastore
aload_0
getfield Life/field [I
astore 95
bipush 45
istore 96
aload 95
iload 96
iconst_0
iastore
aload_0
getfield Life/field [I
astore 97
bipush 46
istore 98
aload 97
iload 98
iconst_0
iastore
aload_0
getfield Life/field [I
astore 99
bipush 47
istore 100
aload 99
iload 100
iconst_0
iastore
aload_0
getfield Life/field [I
astore 101
bipush 48
istore 102
aload 101
iload 102
iconst_0
iastore
aload_0
getfield Life/field [I
astore 103
bipush 49
istore 104
aload 103
iload 104
iconst_0
iastore
aload_0
getfield Life/field [I
astore 105
bipush 50
istore 106
aload 105
iload 106
iconst_0
iastore
aload_0
getfield Life/field [I
astore 107
bipush 51
istore 108
aload 107
iload 108
iconst_0
iastore
aload_0
getfield Life/field [I
astore 109
bipush 52
istore 110
aload 109
iload 110
iconst_0
iastore
aload_0
getfield Life/field [I
astore 111
bipush 53
istore 112
aload 111
iload 112
iconst_0
iastore
aload_0
getfield Life/field [I
astore 113
bipush 54
istore 114
aload 113
iload 114
iconst_0
iastore
aload_0
getfield Life/field [I
astore 115
bipush 55
istore 116
aload 115
iload 116
iconst_0
iastore
aload_0
getfield Life/field [I
astore 117
bipush 56
istore 118
aload 117
iload 118
iconst_0
iastore
aload_0
getfield Life/field [I
astore 119
bipush 57
istore 120
aload 119
iload 120
iconst_0
iastore
aload_0
getfield Life/field [I
astore 121
bipush 58
istore 122
aload 121
iload 122
iconst_0
iastore
aload_0
getfield Life/field [I
astore 123
bipush 59
istore 124
aload 123
iload 124
iconst_0
iastore
aload_0
getfield Life/field [I
astore 125
bipush 60
istore 126
aload 125
iload 126
iconst_0
iastore
aload_0
getfield Life/field [I
astore 127
bipush 61
istore 128
aload 127
iload 128
iconst_0
iastore
aload_0
getfield Life/field [I
astore 129
bipush 62
istore 130
aload 129
iload 130
iconst_0
iastore
aload_0
getfield Life/field [I
astore 131
bipush 63
istore 132
aload 131
iload 132
iconst_0
iastore
aload_0
getfield Life/field [I
astore 133
bipush 64
istore 134
aload 133
iload 134
iconst_0
iastore
aload_0
getfield Life/field [I
astore 135
bipush 65
istore 136
aload 135
iload 136
iconst_0
iastore
aload_0
getfield Life/field [I
astore 137
bipush 66
istore 138
aload 137
iload 138
iconst_0
iastore
aload_0
getfield Life/field [I
astore 139
bipush 67
istore 140
aload 139
iload 140
iconst_0
iastore
aload_0
getfield Life/field [I
astore 141
bipush 68
istore 142
aload 141
iload 142
iconst_0
iastore
aload_0
getfield Life/field [I
astore 143
bipush 69
istore 144
aload 143
iload 144
iconst_0
iastore
aload_0
getfield Life/field [I
astore 145
bipush 70
istore 146
aload 145
iload 146
iconst_0
iastore
aload_0
getfield Life/field [I
astore 147
bipush 71
istore 148
aload 147
iload 148
iconst_0
iastore
aload_0
getfield Life/field [I
astore 149
bipush 72
istore 150
aload 149
iload 150
iconst_0
iastore
aload_0
getfield Life/field [I
astore 151
bipush 73
istore 152
aload 151
iload 152
iconst_0
iastore
aload_0
getfield Life/field [I
astore 153
bipush 74
istore 154
aload 153
iload 154
iconst_0
iastore
aload_0
getfield Life/field [I
astore 155
bipush 75
istore 156
aload 155
iload 156
iconst_0
iastore
aload_0
getfield Life/field [I
astore 157
bipush 76
istore 158
aload 157
iload 158
iconst_0
iastore
aload_0
getfield Life/field [I
astore 159
bipush 77
istore 160
aload 159
iload 160
iconst_0
iastore
aload_0
getfield Life/field [I
astore 161
bipush 78
istore 162
aload 161
iload 162
iconst_0
iastore
aload_0
getfield Life/field [I
astore 163
bipush 79
istore 164
aload 163
iload 164
iconst_0
iastore
aload_0
getfield Life/field [I
astore 165
bipush 80
istore 166
aload 165
iload 166
iconst_0
iastore
aload_0
getfield Life/field [I
astore 167
bipush 81
istore 168
aload 167
iload 168
iconst_0
iastore
aload_0
getfield Life/field [I
astore 169
bipush 82
istore 170
aload 169
iload 170
iconst_0
iastore
aload_0
getfield Life/field [I
astore 171
bipush 83
istore 172
aload 171
iload 172
iconst_0
iastore
aload_0
getfield Life/field [I
astore 173
bipush 84
istore 174
aload 173
iload 174
iconst_0
iastore
aload_0
getfield Life/field [I
astore 175
bipush 85
istore 176
aload 175
iload 176
iconst_0
iastore
aload_0
getfield Life/field [I
astore 177
bipush 86
istore 178
aload 177
iload 178
iconst_0
iastore
aload_0
getfield Life/field [I
astore 179
bipush 87
istore 180
aload 179
iload 180
iconst_0
iastore
aload_0
getfield Life/field [I
astore 181
bipush 88
istore 182
aload 181
iload 182
iconst_0
iastore
aload_0
getfield Life/field [I
astore 183
bipush 89
istore 184
aload 183
iload 184
iconst_0
iastore
aload_0
getfield Life/field [I
astore 185
bipush 90
istore 186
aload 185
iload 186
iconst_0
iastore
aload_0
getfield Life/field [I
astore 187
bipush 91
istore 188
aload 187
iload 188
iconst_0
iastore
aload_0
getfield Life/field [I
astore 189
bipush 92
istore 190
aload 189
iload 190
iconst_0
iastore
aload_0
getfield Life/field [I
astore 191
bipush 93
istore 192
aload 191
iload 192
iconst_0
iastore
aload_0
getfield Life/field [I
astore 193
bipush 94
istore 194
aload 193
iload 194
iconst_0
iastore
aload_0
getfield Life/field [I
astore 195
bipush 95
istore 196
aload 195
iload 196
iconst_0
iastore
aload_0
getfield Life/field [I
astore 197
bipush 96
istore 198
aload 197
iload 198
iconst_0
iastore
aload_0
getfield Life/field [I
astore 199
bipush 97
istore 200
aload 199
iload 200
iconst_0
iastore
aload_0
getfield Life/field [I
astore 201
bipush 98
istore 202
aload 201
iload 202
iconst_0
iastore
aload_0
getfield Life/field [I
astore 203
bipush 99
istore 204
aload 203
iload 204
iconst_0
iastore
aload_0
getfield Life/field [I
astore 205
aload 205
areturn
.end method
.method public update()Z
.limit locals 25
.limit stack 6
aload_0
getfield Life/field [I
astore_1
aload_1
arraylength
istore_2
iload_2
newarray int
astore_3
iconst_0
istore 5
Loop3:
aload_0
getfield Life/field [I
astore 6
aload 6
arraylength
istore 7
iload 5
iload 7
if_icmpge Body3
goto EndLoop3
Body3:
aload_0
getfield Life/field [I
astore 8
aload 8
iload 5
iaload
istore 9
aload_0
iload 5
invokevirtual Life.getLiveNeighborN(I)I
istore 10
iload 9
iconst_1
istore 11
bipush 6
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 12
iload 12
ifne else1
aload_0
getfield Life/UNDERPOP_LIM I
istore 14
aload_0
iload 10
iload 14
invokevirtual Life.ge(II)Z
istore 15
aload_0
getfield Life/OVERPOP_LIM I
istore 16
aload_0
iload 10
iload 16
invokevirtual Life.le(II)Z
istore 17
iload 15
iload 17
iand
istore 18
iload 18
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 19
iload 19
ifne else2
aload_3
iload 5
iconst_0
iastore
goto endif2
else2:
aload_0
getfield Life/field [I
astore 20
aload_3
iload 5
aload 20
iload 5
iaload
iastore
endif2:
goto endif1
else1:
aload_0
getfield Life/REPRODUCE_NUM I
istore 21
aload_0
iload 10
iload 21
invokevirtual Life.eq(II)Z
istore 22
iload 22
ifne else3
aload_3
iload 5
iconst_1
iastore
goto endif3
else3:
aload_0
getfield Life/field [I
astore 23
aload_3
iload 5
aload 23
iload 5
iaload
iastore
endif3:
endif1:
iload 5
iconst_1
iadd
istore 5
EndLoop3:
aload_3
astore 24
aload_0
aload 24
putfield Life/field [I
iconst_1
ireturn
.end method
.method public printField()Z
.limit locals 10
.limit stack 4
iconst_0
istore_1
iconst_0
istore_2
Loop4:
aload_0
getfield Life/field [I
astore_3
aload_3
arraylength
istore 4
iload_1
iload 4
if_icmpge Body4
goto EndLoop4
Body4:
aload_0
getfield Life/xMax I
istore 5
aload_0
iload_2
iload 5
invokevirtual Life.gt(II)Z
istore 6
iload 6
ifne else4
invokestatic io.println()Z
pop
iconst_0
istore_2
goto endif4
else4:
endif4:
aload_0
getfield Life/field [I
astore 8
aload 8
iload_1
iaload
istore 9
iload 9
invokestatic io.print(I)V
iload_1
iconst_1
iadd
istore_1
iload_2
iconst_1
iadd
istore_2
EndLoop4:
invokestatic io.println()V
invokestatic io.println()V
iconst_1
ireturn
.end method
.method public trIdx(II)I
.limit locals 7
.limit stack 2
aload_0
getfield Life/xMax I
istore_3
iload_3
iconst_1
iadd
istore 4
iload 4
iload_2
imul
istore 5
iload_1
iload 5
iadd
istore 6
iload 6
ireturn
.end method
.method public cartIdx(I)[I
.limit locals 11
.limit stack 3
aload_0
getfield Life/xMax I
istore_2
iload_2
iconst_1
iadd
istore_3
iload_1
iload_3
idiv
istore 4
iload 4
iload_3
imul
istore 5
iload_1
iload 5
isub
istore 6
iconst_2
newarray int
astore 7
iconst_0
istore 9
aload 7
iload 9
iload 6
iastore
iconst_1
istore 10
aload 7
iload 10
iload 4
iastore
aload 7
areturn
.end method
.method public getNeighborCoords(I)[I
.limit locals 28
.limit stack 5
aload_0
iload_1
invokevirtual Life.cartIdx(I)[I
astore_2
iconst_0
istore_3
aload_2
iload_3
iaload
istore 4
iconst_1
istore 5
aload_2
iload 5
iaload
istore 6
aload_0
getfield Life/xMax I
istore 7
iload 4
iload 7
if_icmpge else5
iload 4
iconst_1
iadd
istore 8
aload_0
iload 4
iconst_0
invokevirtual Life.gt(II)Z
istore 9
iload 9
ifne else6
iload 4
iconst_1
isub
istore 11
goto endif6
else6:
aload_0
getfield Life/xMax I
istore 12
iload 12
istore 11
endif6:
goto endif5
else5:
iconst_0
istore 8
iload 4
iconst_1
isub
istore 11
endif5:
aload_0
getfield Life/yMax I
istore 13
iload 6
iload 13
if_icmpge else7
iload 6
iconst_1
iadd
istore 14
aload_0
iload 6
iconst_0
invokevirtual Life.gt(II)Z
istore 15
iload 15
ifne else8
iload 6
iconst_1
isub
istore 16
goto endif8
else8:
aload_0
getfield Life/yMax I
istore 17
iload 17
istore 16
endif8:
goto endif7
else7:
iconst_0
istore 14
iload 6
iconst_1
isub
istore 16
endif7:
bipush 8
newarray int
astore 18
iconst_0
istore 20
aload 18
iload 20
aload_0
iload 4
iload 16
invokevirtual Life.trIdx(II)I
iastore
iconst_1
istore 21
aload 18
iload 21
aload_0
iload 11
iload 16
invokevirtual Life.trIdx(II)I
iastore
iconst_2
istore 22
aload 18
iload 22
aload_0
iload 11
iload 6
invokevirtual Life.trIdx(II)I
iastore
iconst_3
istore 23
aload 18
iload 23
aload_0
iload 11
iload 14
invokevirtual Life.trIdx(II)I
iastore
iconst_4
istore 24
aload 18
iload 24
aload_0
iload 4
iload 14
invokevirtual Life.trIdx(II)I
iastore
iconst_5
istore 25
aload 18
iload 25
aload_0
iload 8
iload 14
invokevirtual Life.trIdx(II)I
iastore
bipush 6
istore 26
aload 18
iload 26
aload_0
iload 8
iload 6
invokevirtual Life.trIdx(II)I
iastore
bipush 7
istore 27
aload 18
iload 27
aload_0
iload 8
iload 16
invokevirtual Life.trIdx(II)I
iastore
aload 18
areturn
.end method
.method public getLiveNeighborN(I)I
.limit locals 10
.limit stack 3
iconst_0
istore_2
aload_0
iload_1
invokevirtual Life.getNeighborCoords(I)[I
astore_3
iconst_0
istore 4
Loop5:
aload_3
arraylength
istore 5
iload 4
iload 5
if_icmpge Body5
goto EndLoop5
Body5:
aload_3
iload 4
iaload
istore 6
aload_0
getfield Life/field [I
astore 7
aload 7
iload 6
iaload
istore 8
aload_0
iload 8
iconst_0
invokevirtual Life.ne(II)Z
istore 9
iload 9
ifne else9
iload_2
iconst_1
iadd
istore_2
goto endif9
else9:
endif9:
iload 4
iconst_1
iadd
istore 4
EndLoop5:
iload_2
ireturn
.end method
.method public busyWait(I)Z
.limit locals 5
.limit stack 2
aload_0
getfield Life/LOOPS_PER_MS I
istore_2
iload_1
iload_2
imul
istore_3
iconst_0
istore 4
Loop6:
iload 4
iload_3
if_icmpge Body6
Body6:
iload 4
iconst_1
iadd
istore 4
EndLoop6:
iconst_1
ireturn
.end method
.method public eq(II)Z
.limit locals 8
.limit stack 4
aload_0
iload_1
iload_2
invokevirtual Life.lt(II)Z
istore_3
iconst_1
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 4
iconst_0
ireturn
.end method
.method public ne(II)Z
.limit locals 5
.limit stack 3
aload_0
iload_1
iload_2
invokevirtual Life.eq(II)Z
istore_3
iconst_1
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 4
iload 4
ireturn
.end method
.method public lt(II)Z
.limit locals 4
.limit stack 2
iload_1
iload_2
istore_3
iload_3
ireturn
.end method
.method public le(II)Z
.limit locals 9
.limit stack 4
aload_0
iload_1
iload_2
invokevirtual Life.lt(II)Z
istore_3
iconst_1
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 4
aload_0
iload_1
iload_2
invokevirtual Life.eq(II)Z
istore 5
iconst_3
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 6
iload 4
iload 6
iand
istore 7
iconst_5
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 8
iload 8
ireturn
.end method
.method public gt(II)Z
.limit locals 5
.limit stack 3
aload_0
iload_1
iload_2
invokevirtual Life.le(II)Z
istore_3
iconst_1
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 4
iload 4
ireturn
.end method
.method public ge(II)Z
.limit locals 9
.limit stack 4
aload_0
iload_1
iload_2
invokevirtual Life.gt(II)Z
istore_3
iconst_1
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 4
aload_0
iload_1
iload_2
invokevirtual Life.eq(II)Z
istore 5
iconst_3
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 6
iload 4
iload 6
iand
istore 7
iconst_5
ifne UnaryOperL1
iconst_1
goto UnaryOperL2
UnaryOperL1:
    iconst_0
UnaryOperL2:
istore 8
iload 8
ireturn
.end method