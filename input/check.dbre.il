	.data
	.string	.int_wformat, "%d\12"
	.string	.float_wformat, "%f\12"
	.string	.char_wformat, "%c\12"
	.string	.string_wformat, "%s\12"
	.string	.int_rformat, "%d"
	.string	.float_rformat, "%f"
	.string	.char_rformat, "%c"
	.string	.string_rformat, "%s"
	.string	.string_const_0, "SUCCESS"
	.text
	.frame	main, 0
	loadI	1  => %vr6_0
	i2i	%vr6  => %vr4_0
	loadI	10  => %vr7_0
	comp	%vr4, %vr7 => %vr8_0
	testgt	%vr8  => %vr9_0
	cbr	%vr9  -> .L0
.L1:	nop
	loadI	0  => %vr10_0
	comp	%vr4, %vr10 => %vr11_0
	testlt	%vr11  => %vr12_0
	cbrne	%vr12  -> .L2
	mod	%vr7, %vr10 => %vr13_0
	i2i	%vr13  => %vr5_0
.L2:	nop
	add	%vr4, %vr6 => %vr14_0
	i2i	%vr14  => %vr4_2
	comp	%vr4, %vr7 => %vr8_1
	testle	%vr8  => %vr9_1
	cbr	%vr9  -> .L1
.L0:	nop
	loadI	.string_const_0  => %vr15_0
	swrite	%vr15
	ret
