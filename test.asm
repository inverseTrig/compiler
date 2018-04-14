    .data

promptuser:    .asciiz "Enter value: "
num:           .word 0
f:             .word 0
fi:            .word 0


    .text
main:
# Assignment-Statement
addi    $t0,   $zero, 5
lw      $t1,   fi
mult    $t0,   $t1
mflo    $t0
sw      $t0,   f

# If-Statement
lw      $t0,   num
addi    $t1,   $zero, 3
slt     $t0,   $t0,   $t1
beq     $t0,   $zero, IfStatementFailID8b63a127
# Assignment-Statement
addi    $t0,   $zero, 3
addi    $t1,   $zero, 2
add     $t0,   $t0,   $t1
sw      $t0,   f
j	IfStatementPassID8b63a127
IfStatementFailID8b63a127:
# Assignment-Statement
addi    $t0,   $zero, 3
addi    $t1,   $zero, 2
sub     $t0,   $t0,   $t1
sw      $t0,   f
IfStatementPassID8b63a127:
# Assignment-Statement
addi    $t0,   $zero, 5
sw      $t0,   fi

# Read-Statement
li      $v0,   4
la      $a0,   promptuser
syscall
li      $v0,   5
syscall
sw      $v0,   num

# Write-Statement
addi    $t0,   $zero, 3
addi    $t1,   $zero, 2
add     $s0,   $t0,   $t1
li      $v0,   1
move    $a0,   $s0
syscall

# While-Statement
WhileID955ac4b7:
lw      $t0,   fi
addi    $t1,   $zero, 10
slt     $t0,   $t0,   $t1
beq     $t0,   $zero, WhileCompleteID955ac4b7
# Assignment-Statement
addi    $t0,   $zero, 1
sw      $t0,   f
# Assignment-Statement
lw      $t0,   num
addi    $t1,   $zero, 1
add     $t0,   $t0,   $t1
sw      $t0,   num
j       WhileID955ac4b7
WhileCompleteID955ac4b7:

# If-Statement
lw      $t0,   num
addi    $t1,   $zero, 2
slt     $t0,   $t0,   $t1
beq     $t0,   $zero, IfStatementFailIDcfead843
# Assignment-Statement
addi    $t0,   $zero, 3
sw      $t0,   num
j	IfStatementPassIDcfead843
IfStatementFailIDcfead843:
# Assignment-Statement
addi    $t0,   $zero, 3
sw      $t0,   fi
IfStatementPassIDcfead843:

jr $ra
