\underline{User Story 2}: As a user, I want to sign in, so that I can use e-bike hire service \\
Scenario 1: User sign in with wrong username
\begin{customlist}
\item Given: The user is not registered
\item When: The user sign in with wrong username
\item Then: The system shows an error message, the user is not registered
\end{customlist}
Scenario 2: User sign in with wrong password
\begin{customlist}
\item Given: The user is registered
\item When: The user sign in with wrong password
\item Then: The system shows an error message, the password is wrong
\end{customlist}
Scenario 3: User sign in with empty username or password
\begin{customlist}
\item Given: The user is registered
\item When: The user sign up with empty username or password
\item Then: The system give an error message, the username or password is empty
\end{customlist}
Scenario 4: User sign in with correct username and password
\begin{customlist}
\item Given: The user is registered
\item When: The user sign in with correct username and password
\item Then: User access to the service
\end{customlist}