package monitoring.command;

/**
 * Specifies the action on which the hook should be executed *
 */
public enum Command {
	CREATE,
	RUNNING,
	SHUTDOWN,
	STOP,
	DONE,
	UNKNOWN,
	FAILED,
	CUSTOM,
	
	ERROR,
	DISABLE,
	
	WAKEUP,
	DEPLOY,
	MIGRATE,
	INTERCLOUDMIGRATE,
	
	SHUTDOWN_RACK
}
