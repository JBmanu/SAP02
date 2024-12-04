\underline{User Story 3}: As a user, I want to add credit, so that I can hire e-bike \\
%   domanda: è meglio fare sotto scenari o ogni scenario aggiundere i vincoli nel GIVEN (leggibilità, comprensione) ?
Scenario 1: User is logged in
\begin{customlist}
\item Given: The user has sign in
\item Sub-scenario 1: User add negative credit
\begin{customlist}
\item When: The user add negative credit
\item Then: The system shows an error message, the credit is negative
\end{customlist}
\item Sub-scenario 2: User add some credit
\begin{customlist}
\item When: The user add some credit
\item Then: The system notify add credit
\end{customlist}
\item Sub-scenario 3: User add empty credit
\begin{customlist}
\item When: The user add empty credit
\item Then: The system shows an error message, the credit is empty
\end{customlist}
\end{customlist}