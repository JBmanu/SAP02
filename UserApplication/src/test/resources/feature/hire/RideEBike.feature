\underline{User Story 5}: As a user, I want to ride e-bike, so that I can move \\
Scenario 1: user is logged in and ride e-bike
\begin{customlist}
\item Given: The user has sign in
\item And: The user ride e-bike
\item Sub-scenario 1: User has some credit
\begin{customlist}
\item Given: The user has some credit
\item When: The user ride e-bike
\item Then: The system subtract the credit, notify the user
\end{customlist}
\item Sub-scenario 2: User finished credit
\begin{customlist}
\item Given: The user has no credit
\item When: The user ride e-bike
\item Then: The system stops the ride, notify the user and free the e-bike
\end{customlist}
\end{customlist}