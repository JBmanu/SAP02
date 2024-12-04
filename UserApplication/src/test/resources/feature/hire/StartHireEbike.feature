\underline{User Story 4}: As a user, I want to start ride e-bike, so that I can use it \\
Scenario 1: User is logged in
\begin{customlist}
\item Given: The user has sign in
\item Sub-scenario 1: User start ride e-bike with no credit
\begin{customlist}
\item Given: The user has no credit
\item When: The user start ride e-bike
\item Then: The system shows an error message, the user has no credit
\end{customlist}
\item Sub-scenario 2: User start ride e-bike that is already in use
\begin{customlist}
\item Given: The e-bike is in use
\item When: The user start ride e-bike
\item Then: The system shows an error message, the e-bike is already in use by another user
\end{customlist}
\item Sub-scenario 3: User start ride e-bike with empty battery
\begin{customlist}
\item Given: the e-bike is with empty battery
\item When: The user start ride e-bike
\item Then: The system shows an error message, the e-bike has no battery
\end{customlist}
\item Sub-scenario 4: User start ride e-bike
\begin{customlist}
\item Given: The user has some credit and the e-bike is free and has battery
\item When: The user start ride e-bike
\item Then: The system starts the ride, notify the user, subtract the credit
\item And: change the state of the e-bike
\end{customlist}
\end{customlist}
