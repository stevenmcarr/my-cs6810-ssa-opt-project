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
	loadI	1  => %vr4
	i2i	%vr6  => %vr6
	loadI	10  => %vr4
	comp	%vr4, %vr4 => %vr4
	testgt	%vr4  => %vr4
	cbr	%lr6  -> .L0
.L1:	nop
	loadI	0  => %vr4
	comp	%vr4, %vr4 => %vr4
	testlt	%vr4  => %vr4
	cbrne	%lr9  -> .L2
	loadI	10  => %vr5
	loadI	0  => %vr4
	mod	%vr4, %vr4 => %vr4
	i2i	%vr4  => %vr4
.L2:	nop
	loadI	1  => %vr4
	add	%vr4, %vr4 => %vr4
	i2i	%vr6  => %vr6
	loadI	10  => %vr4
	comp	%vr4, %vr4 => %vr4
	testle	%vr4  => %vr4
	cbr	%lr18  -> .L1
.L0:	nop
	loadI	.string_const_0  => %vr4
	swrite	%lr19
	ret
