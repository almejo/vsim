# VSim

Vsim is digital circuits simulator.

Vsim is a rewrite from scratch of my University Thesis JCSim. You can try JCSim from my [repository](https://github.com/almejo/jcsim).

VSim is in very early stage of development and it lacks a lot of features that JCSim already has. Simulation of gates
and pins is working but it lacks a proper interface.

VSim, as JCSim, will be usefull to understand how digital circuits works. In the future it could be used to teach or
for homework.

## Features

VSim currently has:

- Working simulation
- Start/Stop simulation
- And, Not and Clock gates
- Add connections (remove pressing control and click)
- Add gates (remove pressing control and click)
- Undo and Redo

## Milestone 1.0

The first milestone is to reach feature parity with JCSim. From There all will be new features and exploration of new ideas.

- Add all basic gate types [done]
- Add template gates
- Add debug gates [done]
- Load/Save [done]
- Rotate gates
- Config gates
- Move and Zoom the canvas [in progress]

## Future features

JCSim can do a lot of things. Add/remove gates, interconnect them and simulate the circuit.
But also it have some design problems that make it difficult to add new features. Some things that VSim will have
(and JCSim does not) are:

- Proper cut, copy and paste
- Undo/Redo
- Better interface
- Proper Save and Load. JCSim serialize the complete circuit.
- Color Schemes Suport
- Labels for the objects
- Components tree

## Why a new program? Why not fix JCSim

The main reason is fun. I love to code and doing it from scratch is a lot of fun. Also, this way I can remember all the
process to create it and think again about the design. I am not throwing it away, I am using the best parts, like in the
Princess Bride book.


