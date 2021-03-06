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

package org.formiz.core.input.xls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.formiz.core.FormizMetadata;
import org.formiz.core.impl.ExpressionElement;
import org.formiz.core.impl.ExpressionElementBuilder;

/**
 * Read a XLS file and inject {@link ExpressionElement} into
 * {@link FormizMetadata} instance.
 * <p>
 * 3 columns with the following titles :
 * <ul>
 * <li>GROUP</li>
 * <li>ID</li>
 * <li>EXPRESSION</li>
 *
 */
public class ExpressionXlsInputSource extends AbstractXlsInputSource {

	public ExpressionXlsInputSource(String classpathResource) throws IOException {
		super(classpathResource);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.formiz.core.input.xls.AbstractXlsInputSource#addElement(org.apache.
	 * poi.ss.usermodel.Row, org.apache.poi.ss.usermodel.Row)
	 */
	@Override
	protected void addElement(Row titlesRow, Row currentRow) {
		Map<String, String> line = new HashMap<String, String>();
		RowUtils.toMap(titlesRow, currentRow, line);
		getMetadata().addElement(new ExpressionElementBuilder(getMetadata()).init(line.get("ID"), line.get("GROUP"))
				.expression(line.get("EXPRESSION")).build());
	}

}
