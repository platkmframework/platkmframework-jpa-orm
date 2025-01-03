/**
 * ****************************************************************************
 *  Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	 https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Contributors:
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen.
// Generado el: 2020.04.06 a las 09:02:50 PM CDT
//
package org.platkmframework.jpa.orm.scriptstemplate.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
/**
 * <p>Clase Java para anonymous complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}DataBaseScriptTemplate" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "dataBaseScriptTemplate" })
@XmlRootElement(name = "ScriptsTemplateConfigurations")
public class ScriptsTemplateConfigurations {

    /**
     * ScriptsTemplateConfigurations
     */
    @XmlElement(name = "DataBaseScriptTemplate", required = true)
    protected List<DataBaseScriptTemplate> dataBaseScriptTemplate;

    /**
     * Gets the value of the dataBaseScriptTemplate property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataBaseScriptTemplate property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataBaseScriptTemplate().add(newItem);
     * </pre>
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataBaseScriptTemplate }
     * @return List
     */
    public List<DataBaseScriptTemplate> getDataBaseScriptTemplate() {
        if (dataBaseScriptTemplate == null) {
            dataBaseScriptTemplate = new ArrayList<DataBaseScriptTemplate>();
        }
        return this.dataBaseScriptTemplate;
    }

    /**
     * Constructor ScriptsTemplateConfigurations
     */
    public ScriptsTemplateConfigurations() {
        super();
    }
}
