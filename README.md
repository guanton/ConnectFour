# ConnectFour
Connect Four with simple GUI (Java). 

This was a quick side project that I implemented in a couple days as an exercise to prepare for coding a chess AI.
A friend from UCLA had sent me instructions for a basic connect 4 assignment (PDF attached in repository).

To get 100% on the assignment, you needed to make a "smart AI" that beat a random AI at least 85% of the time.
I did this by making a simple "smart" AI that will always play a winning move if available, and always block
a losing move if possible. This AI beat the random AI about 91% of the time.

However, my goal was to make an even stronger AI using minimax with alpha-beta pruning (to make the program faster). 
As it stands, my minimaxAI beats the random AI 100% of the time (rounded to nearest hundredth), and it beats the "smart" AI 97% of the time.

I think that doing this simple project will be very helpful for my chess project. I developped my skills in:

- GUI with JFrame
- board state generation
- minimax
- pruning


