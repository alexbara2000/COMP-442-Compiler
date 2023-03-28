% start
           entry
           addi   r14,r0,topaddr  % Set stack pointer

           %assigning values
           sub r9,r9,r9
           addi r9,r9,5
           sw t1(r0),r9


           %assigning values
           lw r9,t1(r0)
           sw x(r0),r9


           %assigning values
           sub r9,r9,r9
           addi r9,r9,5
           sw t2(r0),r9


           % processing: t3 := x == t2
           lw r1,x(r0)
           lw r2,t2(r0)
           ceq r3,r1,r2
           sw t3(r0),r3


           %assigning values
           sub r9,r9,r9
           addi r9,r9,6
           sw t4(r0),r9


           %assigning values
           lw r9,t4(r0)
           sw x(r0),r9


           %assigning values
           sub r9,r9,r9
           addi r9,r9,1
           sw t5(r0),r9


           %assigning values
           lw r9,t5(r0)
           sw x(r0),r9


           %assigning values
           sub r9,r9,r9
           addi r9,r9,0
           sw t6(r0),r9


           %assigning values
           lw r9,t6(r0)
           sw y(r0),r9


           %assigning values
           sub r9,r9,r9
           addi r9,r9,0
           sw t7(r0),r9


           %assigning values
           lw r9,t7(r0)
           sw z(r0),r9


           % processing: t8 := x and y
           lw r1,x(r0)
           lw r2,y(r0)
           bz r1,zero1
           bz r2,zero1
           addi r3,r0,1
           j endand1

zero1      addi r3,r0,0
endand1   sw t8(r0),r3



           %assigning values
           lw r9,t8(r0)
           sw y(r0),r9


           % processing: t9 := x and x
           lw r1,x(r0)
           lw r2,x(r0)
           bz r1,zero2
           bz r2,zero2
           addi r3,r0,1
           j endand2

zero2      addi r3,r0,0
endand2   sw t9(r0),r3



           %assigning values
           lw r9,t9(r0)
           sw x(r0),r9


           % processing: t10 := x and x
           lw r1,x(r0)
           lw r2,x(r0)
           bz r1,zero3
           bz r2,zero3
           addi r3,r0,1
           j endand3

zero3      addi r3,r0,0
endand3   sw t10(r0),r3



           %assigning values
           lw r9,t10(r0)
           sw z(r0),r9


           % processing: put(x)
           lw r1,x(r0)
           % put value on stack
           sw -8(r14),r1
           % link buffer to stack
           addi r1,r0, buf
           sw -12(r14),r1
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr


           % processing: put(y)
           lw r1,y(r0)
           % put value on stack
           sw -8(r14),r1
           % link buffer to stack
           addi r1,r0, buf
           sw -12(r14),r1
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr


           % processing: put(z)
           lw r1,z(r0)
           % put value on stack
           sw -8(r14),r1
           % link buffer to stack
           addi r1,r0, buf
           sw -12(r14),r1
           % convert int to string for output
           jl r15, intstr
           sw -8(r14),r13
           % output to console
           jl r15, putstr

           hlt
% space for variable buffer
buf     res 20
% space for variable x
x       res 4
% space for variable y
y       res 4
% space for variable z
z       res 4
% space for variable t1
t1      res 4
% space for variable t2
t2      res 4
% space for x == t2
t3         res 4
% space for variable t4
t4      res 4
% space for variable t5
t5      res 4
% space for variable t6
t6      res 4
% space for variable t7
t7      res 4
% space for x and y
t8         res 4
% space for x and x
t9         res 4
% space for x and x
t10        res 4
