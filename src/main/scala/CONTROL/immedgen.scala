package CONTROL

import chisel3._
import chisel3.util._

class ImmdValGen extends Bundle {
    val instruction = Input ( UInt (32. W ) )
    val pc = Input(UInt(32.W))
	val s_imm = Output(SInt(32.W))
	val sb_imm = Output(SInt(32.W))
	val uj_imm = Output(SInt(32.W))
	val u_imm = Output(SInt(32.W))
	val i_imm = Output(SInt(32.W))
}

class immedgen extends Module {
    val io = IO (new ImmdValGen )
// Start coding here
    //S-type
	val s_imm_ = Cat (io.instruction(31,25),io.instruction(11,7))
	io.s_imm := (Cat(Fill(20,s_imm_(11)),s_imm_)).asSInt
	//SB-type
	val sb_imm_ = Cat (io.instruction(31),io.instruction(7),io.instruction(30,25),io.instruction(11,8),"b0".U)
	io.sb_imm := ((Cat(Fill(19,sb_imm_(12)),sb_imm_)) + io.pc).asSInt
	//UJ-type
	val uj_imm_ = Cat (io.instruction(31),io.instruction(19,12),io.instruction(20),io.instruction(30,21),"b0".U)
	io.uj_imm := ((Cat(Fill(12,uj_imm_(20)),uj_imm_)) + io.pc).asSInt
	//U-type
	io.u_imm := (((Cat(Fill(12,io.instruction(31)),io.instruction(31,12))) << 12)+io.pc).asSInt
	//I-type
	io.i_imm := (Cat(Fill(20,io.instruction(31)),io.instruction(31,20))).asSInt
}



// import chisel3._
// import chisel3.util.Cat
// import chisel3.util.Fill

// class immedgen extends Module {
//     val io = IO(new Bundle {
//         val instruction = Input(UInt(32.W))
//         val pc = Input(UInt(32.W))
//         val s_imm = Output(SInt(32.W))
//         val sb_imm = Output(SInt(32.W))
//         val u_imm = Output(SInt(32.W))
//         val uj_imm = Output(SInt(32.W))
//         val i_imm = Output(SInt(32.W))
//         val lw_imm = Output(SInt(32.W))
//     })
//     // ----- Calculating S-Immediate ------ //

//     val s_lower_half = io.instruction(11,7)     // bits 7-11 from 32 bits
//     val s_upper_half = io.instruction(31,25)    // bits 25-31 from 32 bits
//     var s_imm_12 = Cat(s_upper_half, s_lower_half)    // merging immediates together to form 12 bits
//     val s_imm_32 = Cat(Fill(20, s_imm_12(11)), s_imm_12)   // sign extending 12 bits to 32 bits and merging the 12 bit immediate 
//     io.s_imm := s_imm_32.asSInt


//     // ----- Calculating SB-Immediate ------ //

//     val sb_lower_half = io.instruction(11,8)
//     val sb_upper_half = io.instruction(30, 25)
//     val sb_11thbit = io.instruction(7)
//     val sb_12thbit = io.instruction(31)
//     val sb_imm_13 = Cat(sb_12thbit, sb_11thbit, sb_upper_half, sb_lower_half, 0.S)
//     val sb_imm_32 = Cat(Fill(19, sb_imm_13(12)), sb_imm_13).asSInt
//     io.sb_imm := sb_imm_32 + io.pc.asSInt


//     // ----- Calculating U-Immediate ------ //

//     val u_imm_20 = io.instruction(31,12)
//     var u_imm_32 = Cat(Fill(12, u_imm_20(19)), u_imm_20)
//     // val shift_const = 12.S
//     val u_imm_32_shifted = u_imm_32 << 12.U
//     io.u_imm := u_imm_32_shifted.asSInt+io.pc.asSInt


//     // ----- Calculating UJ-Immediate ----- //
    
//     val uj_lower_half = io.instruction(30, 21)
//     val uj_11thbit = io.instruction(20)
//     val uj_upper_half = io.instruction(19, 12)
//     val uj_20thbit = io.instruction(31)
//     val uj_imm_21 = Cat(uj_20thbit, uj_upper_half, uj_11thbit, uj_lower_half, 0.S)
//     val uj_imm_32 = Cat(Fill(11, uj_imm_21(20)), uj_imm_21).asSInt
//     io.uj_imm := uj_imm_32 


//     // ----- Calculating I-Immediate ----- //

//     val i_imm_12 = io.instruction(31, 20)
//     val i_imm_32 = Cat(Fill(20, i_imm_12(11)), i_imm_12)
//     io.i_imm := i_imm_32.asSInt
//      // ----- Calculating lw-Immediate ----- //

//     val lw_imm_12 = io.instruction(31, 20)
//     val lw_imm_32 = Cat(Fill(20, lw_imm_12(11)), lw_imm_12)
//     io.lw_imm :=lw_imm_32.asSInt
// }