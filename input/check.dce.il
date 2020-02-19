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
	jumpI	-> .L0
.L1:	nop
	jumpI	-> .L2
.L2:	nop
	jumpI	-> .L0
.L0:	nop
	loadI	.string_const_0  => %vr15
	swrite	%vr15
	ret
