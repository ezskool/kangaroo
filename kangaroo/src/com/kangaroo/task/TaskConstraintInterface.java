package com.kangaroo.task;

/**
 * This interface has to be implemented by all classes, that will be used as constraints in the Task-Class.
 * @author alex
 *
 */
public interface TaskConstraintInterface 
{
	
	/**
	 * This method returns the type of this concrete TaskConstraint.
	 * Depending on the type a different validation method is used the different constraints.
	 * 
	 * @return String: the type of this concrete TaskConstraint. TODO: String is not a good choice here
	 */
	public String getType();
	
}
