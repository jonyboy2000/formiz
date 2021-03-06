/**
 *  Copyright SCN Guichet Entreprises, Capgemini and contributors (2014-2016)
 *
 * This software is a computer program whose purpose is to [describe
 * functionalities and technical features of your software].
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package org.formiz.core.expr.js;

import java.util.HashMap;
import java.util.Map;

/**
 *  Copyright SCN Guichet Entreprises, Capgemini and contributors (2014-2016)
 *
 * This software is a computer program whose purpose is to [describe
 * functionalities and technical features of your software].
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

import org.formiz.core.expr.IContext;
import org.formiz.core.expr.IExpression;
import org.formiz.core.expr.impl.ContextBuilder;
import org.formiz.core.expr.impl.FrenchExpressionParser;
import org.formiz.core.expr.js.dummy.DummyExpression;
import org.formiz.core.expr.js.dummy.DummyExpressionParser;
import org.formiz.core.expr.js.model.Box;
import org.formiz.core.expr.test1.model.Address;
import org.formiz.core.expr.test1.model.Functions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrenchExpressionTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrenchExpressionTest.class);

	FrenchExpressionParser parser = new FrenchExpressionParser(new JsExpressionParser());

	@Test
	public void companyValidTest() {
		String exprText = "(company est FAUX )  OU (company est VRAI ET companyName n'est pas absent)";
		IExpression e = parser.parseExpression(exprText);

		// Context
		Address a = new Address();
		IContext context = new JsContext();
		context.setObject(a, null);

		a.setCompany(false);
		Assert.assertTrue((Boolean) e.getValue(context));

		a.setCompany(true);
		Assert.assertFalse((Boolean) e.getValue(context));

		a.setCompany(true);
		a.setCompanyName("Formiz");
		Assert.assertTrue((Boolean) e.getValue(context));

	}

	@Test
	public void dummyTest() {
		String exprText = "companyName est égal à \"formiz\"";
		String exprText2 = "companyName n'est pas égal à \"formiz\"";
		FrenchExpressionParser parser = new FrenchExpressionParser(new DummyExpressionParser());

		IExpression e1 = parser.parseExpression(exprText);
		IExpression e2 = parser.parseExpression(exprText2);

		Assert.assertEquals("companyName == \"formiz\"", ((DummyExpression) e1).getExpression());
		Assert.assertEquals("companyName != \"formiz\"", ((DummyExpression) e2).getExpression());

	}

	@Test
	public void egalTest() {
		String exprText = "companyName est égal à \"formiz\"";
		String exprText2 = "companyName n'est pas égal à \"formiz\"";
		IExpression e1 = parser.parseExpression(exprText);
		IExpression e2 = parser.parseExpression(exprText2);

		// Context
		Address a = new Address();
		IContext context = new JsContext();
		context.setObject(a, null);

		a.setCompanyName("formiz");
		Assert.assertTrue((Boolean) e1.getValue(context));
		Assert.assertFalse((Boolean) e2.getValue(context));

		a.setCompanyName("formiz2");
		Assert.assertFalse((Boolean) e1.getValue(context));
		Assert.assertTrue((Boolean) e2.getValue(context));

	}

	@Test
	public void functionTest() throws ClassNotFoundException {
		String exprText = "_sum( 1, 2) est égal à 3";
		IExpression e1 = parser.parseExpression(exprText);
		IContext context = new JsContext();

		ContextBuilder cb = new ContextBuilder();
		Map<String, String> functions = new HashMap<String, String>();
		functions.put("sum", Functions.class.getName() + ".sum");
		cb.setFunctions(functions);
		cb.init();
		cb.build(context);

		Assert.assertTrue((Boolean) e1.getValue(context));
	}

	@Test
	public void inferieurSuperieurTest() {

		String exprText = "size est inférieur à 10";
		String exprText2 = "size est supérieur à 10";
		IExpression e1 = parser.parseExpression(exprText);
		IExpression e2 = parser.parseExpression(exprText2);

		// Context
		IContext context = new JsContext();

		context.setObject(new Box(5), null);
		Assert.assertTrue((Boolean) e1.getValue(context));
		Assert.assertFalse((Boolean) e2.getValue(context));

		context.setObject(new Box(15), null);
		Assert.assertFalse((Boolean) e1.getValue(context));
		Assert.assertTrue((Boolean) e2.getValue(context));

	}

	@Test
	public void variableTest() throws ClassNotFoundException {
		String exprText = "_id est égal à 3";
		IExpression e1 = parser.parseExpression(exprText);
		IContext context = new JsContext();

		ContextBuilder cb = new ContextBuilder();
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("id", Functions.class.getName() + ".id");
		cb.setVariableProviders(variables);
		cb.init();
		cb.build(context);

		Assert.assertTrue((Boolean) e1.getValue(context));
	}

	@Test
	public void videTest() {
		String exprText = "companyName est absent";
		String exprText2 = "companyName n'est pas absent";
		IExpression e1 = parser.parseExpression(exprText);
		IExpression e2 = parser.parseExpression(exprText2);

		// Context
		Address a = new Address();
		IContext context = new JsContext();
		context.setObject(a, null);

		a.setCompanyName(null);
		Assert.assertTrue((Boolean) e1.getValue(context));
		Assert.assertFalse((Boolean) e2.getValue(context));

		a.setCompanyName("Formiz");
		Assert.assertFalse((Boolean) e1.getValue(context));
		Assert.assertTrue((Boolean) e2.getValue(context));

	}

	@Test
	public void vraiTest() {
		String exprText = "company est VRAI";
		IExpression e = parser.parseExpression(exprText);

		// Context
		Address a = new Address();
		IContext context = new JsContext();
		context.setObject(a, null);

		a.setCompany(false);
		Assert.assertFalse((Boolean) e.getValue(context));

		a.setCompany(true);
		Assert.assertTrue((Boolean) e.getValue(context));

		// Test getText
		Assert.assertEquals(exprText, e.getText());
	}

}
