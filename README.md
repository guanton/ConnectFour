# ConnectFour
Connect Four with simple GUI (Java). 

This was a quick side project that I implemented in a couple days as an exercise to prepare for coding a chess AI.
A friend from UCLA had sent me instructions for a basic connect 4 assignment (PDF attached in repository).

To get 100% on the assignment, you needed to make a "smart AI" that beat a random AI at least 85% of the time.
I did this by making a simple "smart" AI that will always play a winning move if available, and always block
a losing move if possible. This AI beat the random AI about 96% of the time.

However, my goal was to make an even stronger AI using minimax, and I incoporated alpha-beta pruning to make the program run faster. 
As it stands, my minimaxAI beats the random AI and the "smart" AI 100% of the time (rounded to nearest hundredth). Since Connect Four is a perfect game, it is obviously still possible to beat my AI, but it is pretty difficult, and I am continuing to make small tweaks to make my AI even stronger. Given what I have read in research papers on the subject, although it is possible to code a perfect Connect Four AI, this requires a good deal of hardcoding.

I think that doing this simple project should be helpful for developing a chess engine. I developped my skills in:

- GUI with JFrame
- board state generation
- minimax
- pruning


