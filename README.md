# VSim

Vsim is digital circuits simulator. 

Vsim is a rewrite from scratch of my University Thesis JCSim. You can try JCSim from my [repository](https://github.com/almejo/jcsim).

VSim is in very active development and it still lacks a some of the features that JCSim already has. Right now it is posible to draw simple circuits and save/load them. 

VSim, as JCSim, will be usefull to understand how digital circuits works. In the future it could be used to teach or
for homework.

## Features

VSim currently has:

- Working simulation
- Start/Stop simulation
- Basic gates: And, Not, FlipFlopData, etc.
- Debug gates: clock, seven-segments display, a time diagram.
- Add connections (remove pressing control and click)
- Add gates (remove pressing control and click)
- Undo and Redo
- Move, Resize and Zoom the canvas
- Select a color-scheme

## Milestone 1.0

The first milestone is to reach feature parity with JCSim. From There all will be new features and exploration of new ideas.

- Add all basic gate types [done]
- Add template gates
- Add debug gates [done]
- Load/Save [done]
- Rotate gates
- Config gates
- Move the canvas [done]
- Zoom the canvas [done]

## Future features

JCSim can do a lot of things. Add/remove gates, interconnect them and simulate the circuit.
But also it have some design problems that make it difficult to add new features. Some things that VSim will have
(and JCSim does not) are:

- Proper cut, copy and paste
- Undo/Redo [done]
- Better interface  [done]
- Proper Save and Load. JCSim serialize the complete circuit.  [done]
- Color Schemes Suport  [done]
- Labels for the objects 
- Components tree

## Why a new program? Why not fix JCSim

The main reason is fun. I love to code and doing it from scratch is a lot of fun. Also, this way I can remember all the
process to create it and think again about the design. I am not throwing it away, I am using the best parts, like in the
Princess Bride book.

## How can you help.

There are a lot of ways you can help. If you see something not working you can create a new issue in github. You can help me translating it to other languages (the interface is only in spanish right now).

You can always reach me at alejandro.vera at gmail.com
