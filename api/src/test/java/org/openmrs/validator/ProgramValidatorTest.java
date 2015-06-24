/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.validator;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Tests methods on the {@link ProgramValidator} class.
 */
public class ProgramValidatorTest extends BaseContextSensitiveTest {
	
	@Autowired
	protected Validator programValidator;
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 */
	@Test
	@Verifies(value = "should fail validation if name is null or empty or whitespace", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfNameIsNullOrEmptyOrWhitespace() throws Exception {
		Program prog = new Program();
		prog.setName(null);
		prog.setConcept(Context.getConceptService().getConcept(3));
		
		Errors errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertTrue(errors.hasFieldErrors("name"));
		
		prog.setName("");
		errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertTrue(errors.hasFieldErrors("name"));
		
		prog.setName(" ");
		errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertTrue(errors.hasFieldErrors("name"));
	}
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 */
	@Test
	@Verifies(value = "should fail validation if program name already in use", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfProgramNameAlreadyInUse() throws Exception {
		Program prog = new Program();
		prog.setName("MDR-TB PROGRAM");
		prog.setConcept(Context.getConceptService().getConcept(3));
		
		Errors errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertTrue(errors.hasFieldErrors("name"));
	}
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 */
	@Test
	@Verifies(value = "should pass validation if description is null or empty or whitespace", method = "validate(Object,Errors)")
	public void validate_shouldPassValidationIfDescriptionIsNullOrEmptyOrWhitespace() throws Exception {
		
		Program prog = new Program();
		prog.setDescription(null);
		prog.setConcept(Context.getConceptService().getConcept(3));
		
		Errors errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertFalse(errors.hasFieldErrors("description"));
		
		prog.setDescription("");
		errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertFalse(errors.hasFieldErrors("description"));
		
		prog.setDescription(" ");
		errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertFalse(errors.hasFieldErrors("description"));
	}
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 */
	@Test
	@Verifies(value = "should fail validation if concept is null or empty or whitespace", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfConceptIsNullOrEmptyOrWhitespace() throws Exception {
		Program prog = new Program();
		prog.setName("Hypochondriasis program");
		prog.setConcept(null);
		
		Errors errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		Assert.assertTrue(errors.hasFieldErrors("concept"));
	}
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 */
	@Test
	@Verifies(value = "should pass validation if all required fields have proper values", method = "validate(Object,Errors)")
	public void validate_shouldPassValidationIfAllRequiredFieldsHaveProperValues() throws Exception {
		Program prog = new Program();
		prog.setName("Hypochondriasis program");
		prog.setDescription("This is Hypochondriasis program");
		prog.setConcept(Context.getConceptService().getConcept(3));
		
		Errors errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		
		Assert.assertFalse(errors.hasErrors());
	}
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 * @verifies pass validation and save edited program
	 */
	@Test
	public void validate_shouldPassValidationAndSaveEditedProgram() throws Exception {
		Program program = Context.getProgramWorkflowService().getProgram(3);
		program.setDescription("Edited description");
		
		Errors errors = new BindException(program, "program");
		programValidator.validate(program, errors);
		
		Assert.assertFalse(errors.hasErrors());
		
		Context.getProgramWorkflowService().saveProgram(program);
		program = Context.getProgramWorkflowService().getProgram(3);
		
		Assert.assertTrue(program.getDescription().equals("Edited description"));
	}
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 */
	@Test
	@Verifies(value = "should pass validation if field lengths are correct", method = "validate(Object,Errors)")
	public void validate_shouldPassValidationIfFieldLengthsAreCorrect() throws Exception {
		Program prog = new Program();
		prog.setName("Hypochondriasis program");
		prog.setConcept(Context.getConceptService().getConcept(3));
		
		Errors errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		
		Assert.assertFalse(errors.hasErrors());
	}
	
	/**
	 * @see ProgramValidator#validate(Object,Errors)
	 */
	@Test
	@Verifies(value = "should fail validation if field lengths are not correct", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfFieldLengthsAreNotCorrect() throws Exception {
		Program prog = new Program();
		prog
		        .setName("too long text too long text too long text too long text too long text too long text too long text too long text");
		prog.setConcept(Context.getConceptService().getConcept(3));
		
		Errors errors = new BindException(prog, "prog");
		programValidator.validate(prog, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("name"));
	}
}
