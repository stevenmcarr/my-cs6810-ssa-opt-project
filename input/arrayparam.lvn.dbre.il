	.data
	.string	.int_wformat, "%d\12"
	.string	.float_wformat, "%f\12"
	.string	.char_wformat, "%c\12"
	.string	.string_wformat, "%s\12"
	.string	.int_rformat, "%d"
	.string	.float_rformat, "%f"
	.string	.char_rformat, "%c"
	.string	.string_rformat, "%s"
	.float	.float_const_0, 0.01
	.global	main_fp, 4, 4
	.text
	.frame	main, 40
	loadI	main_fp  => %vr4
	store	%vr0  => %vr4
	addI	%vr0, -4 => %vr6
	call	%vr6, %vr6 
	call	%vr6, %vr6 
	loadI	1  => %vr4
	loadI	10  => %vr8
	comp	%vr4, %vr8 => %vr9
	testgt	%vr9  => %vr10
	cbr	%vr10  -> .L0
.L1:	nop
	i2i	%vr0  => %vr11
	addI	%vr11, -4 => %vr12
	subI	%vr4, 1 => %vr13
	multI	%vr13, 4 => %vr15
	sub	%vr12, %vr15 => %vr16
	load	%vr16  => %vr17
	mult	%vr4, %vr17 => %vr18
	store	%vr18  => %vr16
	call	%vr16, %vr16 
	addI	%vr4, 1 => %vr19
	i2i	%vr19  => %vr4
	comp	%vr4, %vr8 => %vr9
	testle	%vr9  => %vr10
	cbr	%vr10  -> .L1
.L0:	nop
	call	%vr6, %vr6 
	ret
	.frame	init, 0, %vr8
	loadI	1  => %vr4
	loadI	10  => %vr10
	comp	%vr4, %vr10 => %vr11
	testgt	%vr11  => %vr12
	cbr	%vr12  -> .L2
.L3:	nop
	i2f	%vr4  => %vr13
	loadI	.float_const_0  => %vr14
	fload	%vr14  => %vr15
	fmult	%vr13, %vr15 => %vr16
	i2f	%vr5  => %vr17
	fadd	%vr16, %vr17 => %vr18
	f2i	%vr18  => %vr19
	subI	%vr4, 1 => %vr20
	multI	%vr20, 4 => %vr22
	sub	%vr8, %vr22 => %vr23
	store	%vr19  => %vr23
	addI	%vr4, 1 => %vr24
	i2i	%vr24  => %vr4
	addI	%vr5, 1 => %vr25
	i2i	%vr25  => %vr5
	comp	%vr4, %vr10 => %vr11
	testle	%vr11  => %vr12
	cbr	%vr12  -> .L3
.L2:	nop
	ret
	.frame	writearray, 0, %vr4
	loadI	0  => %vr6
	multI	%vr6, 4 => %vr8
	sub	%vr4, %vr8 => %vr9
	load	%vr9  => %vr10
	iwrite	%vr10
	loadI	1  => %vr12
	multI	%vr12, 4 => %vr13
	sub	%vr4, %vr13 => %vr14
	load	%vr14  => %vr15
	iwrite	%vr15
	loadI	2  => %vr17
	multI	%vr17, 4 => %vr18
	sub	%vr4, %vr18 => %vr19
	load	%vr19  => %vr20
	iwrite	%vr20
	loadI	3  => %vr21
	multI	%vr21, 4 => %vr22
	sub	%vr4, %vr22 => %vr23
	load	%vr23  => %vr24
	iwrite	%vr24
	loadI	4  => %vr26
	multI	%vr26, 4 => %vr27
	sub	%vr4, %vr27 => %vr28
	load	%vr28  => %vr29
	iwrite	%vr29
	loadI	5  => %vr31
	multI	%vr31, 4 => %vr32
	sub	%vr4, %vr32 => %vr33
	load	%vr33  => %vr34
	iwrite	%vr34
	loadI	6  => %vr36
	multI	%vr36, 4 => %vr37
	sub	%vr4, %vr37 => %vr38
	load	%vr38  => %vr39
	iwrite	%vr39
	loadI	7  => %vr41
	multI	%vr41, 4 => %vr42
	sub	%vr4, %vr42 => %vr43
	load	%vr43  => %vr44
	iwrite	%vr44
	loadI	8  => %vr46
	multI	%vr46, 4 => %vr47
	sub	%vr4, %vr47 => %vr48
	load	%vr48  => %vr49
	iwrite	%vr49
	loadI	9  => %vr51
	multI	%vr51, 4 => %vr52
	sub	%vr4, %vr52 => %vr53
	load	%vr53  => %vr54
	iwrite	%vr54
	ret
	.frame	inc, 0, %vr4
	load	%vr4  => %vr5
	addI	%vr5, 1 => %vr7
	store	%vr7  => %vr4
	ret
