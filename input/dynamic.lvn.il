	.data
	.string	.int_wformat, "%d\12"
	.string	.float_wformat, "%f\12"
	.string	.char_wformat, "%c\12"
	.string	.string_wformat, "%s\12"
	.string	.int_rformat, "%d"
	.string	.float_rformat, "%f"
	.string	.char_rformat, "%c"
	.string	.string_rformat, "%s"
	.text
	.frame	main, 1820
	loadI	0  => %vr9
	loadI	1  => %vr10
	i2i	%vr0  => %vr11
	addI	%vr11, -4 => %vr13
	loadI	4  => %vr15
	i2i	%vr13  => %vr17
	store	%vr9  => %vr17
	loadI	2  => %vr18
	subI	%vr13, 4 => %vr21
	store	%vr10  => %vr21
	loadI	3  => %vr22
	loadI	8  => %vr24
	subI	%vr13, 8 => %vr25
	store	%vr18  => %vr25
	subI	%vr13, 12 => %vr28
	store	%vr22  => %vr28
	loadI	5  => %vr29
	subI	%vr13, 16 => %vr32
	store	%vr15  => %vr32
	loadI	6  => %vr33
	loadI	20  => %vr35
	subI	%vr13, 20 => %vr36
	store	%vr29  => %vr36
	loadI	7  => %vr37
	subI	%vr13, 24 => %vr40
	store	%vr33  => %vr40
	subI	%vr13, 28 => %vr44
	store	%vr37  => %vr44
	i2i	%vr24  => %vr41
	loadI	9  => %vr45
	subI	%vr13, 32 => %vr48
	store	%vr41  => %vr48
	subI	%vr13, 36 => %vr52
	store	%vr45  => %vr52
	i2i	%vr24  => %vr41
	loadI	11  => %vr53
	subI	%vr13, 40 => %vr56
	store	%vr41  => %vr56
	subI	%vr13, 44 => %vr60
	store	%vr37  => %vr60
	subI	%vr13, 48 => %vr64
	store	%vr33  => %vr64
	subI	%vr13, 52 => %vr68
	store	%vr29  => %vr68
	subI	%vr13, 56 => %vr72
	store	%vr15  => %vr72
	subI	%vr13, 60 => %vr76
	store	%vr22  => %vr76
	subI	%vr13, 64 => %vr80
	store	%vr18  => %vr80
	subI	%vr13, 68 => %vr84
	store	%vr10  => %vr84
	subI	%vr13, 72 => %vr88
	store	%vr9  => %vr88
	addI	%vr11, -80 => %vr90
	i2i	%vr90  => %vr91
	store	%vr9  => %vr91
	subI	%vr90, 4 => %vr92
	store	%vr10  => %vr92
	subI	%vr90, 8 => %vr93
	store	%vr22  => %vr93
	subI	%vr90, 12 => %vr94
	store	%vr29  => %vr94
	subI	%vr90, 16 => %vr95
	store	%vr37  => %vr95
	subI	%vr90, 20 => %vr96
	store	%vr45  => %vr96
	subI	%vr90, 24 => %vr97
	store	%vr53  => %vr97
	subI	%vr90, 28 => %vr98
	store	%vr15  => %vr98
	subI	%vr90, 32 => %vr99
	store	%vr22  => %vr99
	subI	%vr90, 36 => %vr100
	store	%vr18  => %vr100
	subI	%vr90, 40 => %vr101
	store	%vr10  => %vr101
	subI	%vr90, 44 => %vr102
	store	%vr45  => %vr102
	i2i	%vr24  => %vr41
	subI	%vr90, 48 => %vr103
	store	%vr41  => %vr103
	subI	%vr90, 52 => %vr104
	store	%vr37  => %vr104
	subI	%vr90, 56 => %vr105
	store	%vr33  => %vr105
	subI	%vr90, 60 => %vr106
	store	%vr9  => %vr106
	i2i	%vr35  => %vr8
	i2i	%vr10  => %vr4
	i2i	%vr21  => %vr111
	load	%vr111  => %vr112
	comp	%vr112, %vr9 => %vr113
	testeq	%vr113  => %vr114
	cbr	%vr114  -> .L0
.L1:	nop
	subI	%vr4, 1 => %vr115
	i2i	%vr8  => %vr116
	add	%vr4, %vr116 => %vr117
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr117, 1 => %vr120
	multI	%vr120, 4 => %vr121
	sub	%vr119, %vr121 => %vr122
	store	%vr115  => %vr122
	addI	%vr4, 1 => %vr108
	i2i	%vr108  => %vr4
	addI	%vr4, 1 => %vr108
	addI	%vr11, -4 => %vr13
	subI	%vr108, 1 => %vr109
	multI	%vr109, 4 => %vr110
	sub	%vr13, %vr110 => %vr111
	load	%vr111  => %vr112
	loadI	0  => %vr9
	comp	%vr112, %vr9 => %vr113
	testne	%vr113  => %vr114
	cbr	%vr114  -> .L1
.L0:	nop
	loadI	1  => %vr10
	i2i	%vr10  => %vr5
	i2i	%vr0  => %vr11
	addI	%vr11, -80 => %vr90
	subI	%vr90, 4 => %vr126
	load	%vr126  => %vr127
	loadI	0  => %vr9
	comp	%vr127, %vr9 => %vr128
	testeq	%vr128  => %vr129
	cbr	%vr129  -> .L2
.L3:	nop
	subI	%vr5, 1 => %vr130
	mult	%vr5, %vr8 => %vr131
	addI	%vr131, 1 => %vr132
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr132, 1 => %vr133
	multI	%vr133, 4 => %vr134
	sub	%vr119, %vr134 => %vr135
	store	%vr130  => %vr135
	addI	%vr5, 1 => %vr123
	i2i	%vr123  => %vr5
	addI	%vr5, 1 => %vr123
	addI	%vr11, -80 => %vr90
	subI	%vr123, 1 => %vr124
	multI	%vr124, 4 => %vr125
	sub	%vr90, %vr125 => %vr126
	load	%vr126  => %vr127
	loadI	0  => %vr9
	comp	%vr127, %vr9 => %vr128
	testne	%vr128  => %vr129
	cbr	%vr129  -> .L3
.L2:	nop
	loadI	2  => %vr18
	i2i	%vr18  => %vr6
	comp	%vr6, %vr5 => %vr136
	testgt	%vr136  => %vr137
	cbr	%vr137  -> .L4
.L5:	nop
	loadI	2  => %vr18
	i2i	%vr18  => %vr7
	comp	%vr7, %vr4 => %vr138
	testgt	%vr138  => %vr139
	cbr	%vr139  -> .L6
.L7:	nop
	subI	%vr6, 1 => %vr140
	mult	%vr140, %vr8 => %vr141
	subI	%vr7, 1 => %vr142
	add	%vr141, %vr142 => %vr143
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr143, 1 => %vr144
	multI	%vr144, 4 => %vr145
	sub	%vr119, %vr145 => %vr146
	load	%vr146  => %vr147
	mult	%vr6, %vr8 => %vr148
	add	%vr7, %vr148 => %vr149
	subI	%vr149, 1 => %vr150
	multI	%vr150, 4 => %vr151
	sub	%vr119, %vr151 => %vr152
	store	%vr147  => %vr152
	addI	%vr11, -80 => %vr90
	multI	%vr140, 4 => %vr153
	sub	%vr90, %vr153 => %vr154
	load	%vr154  => %vr155
	addI	%vr11, -4 => %vr13
	multI	%vr142, 4 => %vr156
	sub	%vr13, %vr156 => %vr157
	load	%vr157  => %vr158
	comp	%vr155, %vr158 => %vr159
	testne	%vr159  => %vr160
	cbrne	%vr160  -> .L8
	mult	%vr6, %vr8 => %vr148
	add	%vr148, %vr7 => %vr149
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr149, 1 => %vr150
	multI	%vr150, 4 => %vr151
	sub	%vr119, %vr151 => %vr152
	load	%vr152  => %vr161
	addI	%vr161, 1 => %vr162
	store	%vr162  => %vr152
.L8:	nop
	subI	%vr6, 1 => %vr140
	mult	%vr140, %vr8 => %vr141
	add	%vr141, %vr7 => %vr163
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr163, 1 => %vr164
	multI	%vr164, 4 => %vr165
	sub	%vr119, %vr165 => %vr166
	load	%vr166  => %vr167
	mult	%vr6, %vr8 => %vr148
	add	%vr7, %vr148 => %vr149
	subI	%vr149, 1 => %vr150
	multI	%vr150, 4 => %vr151
	sub	%vr119, %vr151 => %vr152
	load	%vr152  => %vr161
	comp	%vr167, %vr161 => %vr168
	testlt	%vr168  => %vr169
	cbrne	%vr169  -> .L9
	subI	%vr6, 1 => %vr140
	mult	%vr140, %vr8 => %vr141
	add	%vr141, %vr7 => %vr163
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr163, 1 => %vr164
	multI	%vr164, 4 => %vr165
	sub	%vr119, %vr165 => %vr166
	load	%vr166  => %vr167
	addI	%vr167, 1 => %vr170
	mult	%vr6, %vr8 => %vr148
	add	%vr7, %vr148 => %vr149
	subI	%vr149, 1 => %vr150
	multI	%vr150, 4 => %vr151
	sub	%vr119, %vr151 => %vr152
	store	%vr170  => %vr152
.L9:	nop
	mult	%vr6, %vr8 => %vr148
	subI	%vr7, 1 => %vr142
	add	%vr148, %vr142 => %vr171
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr171, 1 => %vr172
	multI	%vr172, 4 => %vr173
	sub	%vr119, %vr173 => %vr174
	load	%vr174  => %vr175
	add	%vr148, %vr7 => %vr149
	subI	%vr149, 1 => %vr150
	multI	%vr150, 4 => %vr151
	sub	%vr119, %vr151 => %vr152
	load	%vr152  => %vr161
	comp	%vr175, %vr161 => %vr176
	testlt	%vr176  => %vr177
	cbrne	%vr177  -> .L10
	mult	%vr6, %vr8 => %vr148
	subI	%vr7, 1 => %vr142
	add	%vr148, %vr142 => %vr171
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr171, 1 => %vr172
	multI	%vr172, 4 => %vr173
	sub	%vr119, %vr173 => %vr174
	load	%vr174  => %vr175
	addI	%vr175, 1 => %vr178
	add	%vr148, %vr7 => %vr149
	subI	%vr149, 1 => %vr150
	multI	%vr150, 4 => %vr151
	sub	%vr119, %vr151 => %vr152
	store	%vr178  => %vr152
.L10:	nop
	addI	%vr7, 1 => %vr179
	i2i	%vr179  => %vr7
	comp	%vr7, %vr4 => %vr138
	testle	%vr138  => %vr139
	cbr	%vr139  -> .L7
.L6:	nop
	addI	%vr6, 1 => %vr180
	i2i	%vr180  => %vr6
	comp	%vr6, %vr5 => %vr136
	testle	%vr136  => %vr137
	cbr	%vr137  -> .L5
.L4:	nop
	mult	%vr5, %vr8 => %vr131
	add	%vr131, %vr4 => %vr181
	i2i	%vr0  => %vr11
	addI	%vr11, -144 => %vr119
	subI	%vr181, 1 => %vr182
	multI	%vr182, 4 => %vr183
	sub	%vr119, %vr183 => %vr184
	load	%vr184  => %vr185
	iwrite	%vr185
	ret
