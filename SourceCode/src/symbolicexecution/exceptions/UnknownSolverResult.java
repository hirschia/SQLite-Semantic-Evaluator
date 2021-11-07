package symbolicexecution.exceptions;

public class UnknownSolverResult extends RuntimeException {
	public UnknownSolverResult() {
		super("Z3 Solver could not determine the satisfiability of a check constraint.");
	}
}
