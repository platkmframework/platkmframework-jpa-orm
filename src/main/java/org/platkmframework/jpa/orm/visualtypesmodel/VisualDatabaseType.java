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
// Generado el: 2020.05.27 a las 11:44:33 AM CDT
//
package org.platkmframework.jpa.orm.visualtypesmodel;

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
 *         &lt;element ref="{}id"/>
 *         &lt;element ref="{}name"/>
 *         &lt;element ref="{}type"/>
 *         &lt;element ref="{}tagType"/>
 *         &lt;element ref="{}description"/>
 *         &lt;element ref="{}active"/>
 *         &lt;element ref="{}validMethodName"/>
 *         &lt;element ref="{}typeFormat"/>
 *         &lt;element ref="{}contraintName"/>
 *         &lt;element ref="{}contrianReferences"/>
 *         &lt;element ref="{}hideSize"/>
 *         &lt;element ref="{}languageType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "name", "type", "tagType", "description", "active", "validMethodName", "typeFormat", "contraintName", "contrianReferences", "hideSize", "languageType" })
@XmlRootElement(name = "VisualDatabaseType")
public class VisualDatabaseType {

    /**
     * Atributo id
     */
    @XmlElement(required = true)
    protected String id;

    /**
     * Atributo name
     */
    @XmlElement(required = true)
    protected String name;

    /**
     * Atributo type
     */
    @XmlElement(required = true)
    protected String type;

    /**
     * Atributo tagType
     */
    @XmlElement(required = true)
    protected String tagType;

    /**
     * Atributo description
     */
    @XmlElement(required = true)
    protected String description;

    /**
     * Atributo active
     */
    protected boolean active;

    /**
     * Atributo validMethodName
     */
    @XmlElement(required = true)
    protected String validMethodName;

    /**
     * Atributo typeFormat
     */
    @XmlElement(required = true)
    protected String typeFormat;

    /**
     * Atributo contraintName
     */
    @XmlElement(required = true)
    protected String contraintName;

    /**
     * Atributo contrianReferences
     */
    @XmlElement(required = true)
    protected String contrianReferences;

    /**
     * Atributo hideSize
     */
    protected boolean hideSize;

    /**
     * VisualDatabaseType
     */
    @XmlElement(required = true)
    protected String languageType;

    /**
     * Obtiene el valor de la propiedad id.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad name.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtiene el valor de la propiedad type.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Define el valor de la propiedad type.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtiene el valor de la propiedad tagType.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getTagType() {
        return tagType;
    }

    /**
     * Define el valor de la propiedad tagType.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setTagType(String value) {
        this.tagType = value;
    }

    /**
     * Obtiene el valor de la propiedad description.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Define el valor de la propiedad description.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Obtiene el valor de la propiedad active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Define el valor de la propiedad active.
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Obtiene el valor de la propiedad validMethodName.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getValidMethodName() {
        return validMethodName;
    }

    /**
     * Define el valor de la propiedad validMethodName.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setValidMethodName(String value) {
        this.validMethodName = value;
    }

    /**
     * Obtiene el valor de la propiedad typeFormat.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getTypeFormat() {
        return typeFormat;
    }

    /**
     * Define el valor de la propiedad typeFormat.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setTypeFormat(String value) {
        this.typeFormat = value;
    }

    /**
     * Obtiene el valor de la propiedad contraintName.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getContraintName() {
        return contraintName;
    }

    /**
     * Define el valor de la propiedad contraintName.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setContraintName(String value) {
        this.contraintName = value;
    }

    /**
     * Obtiene el valor de la propiedad contrianReferences.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getContrianReferences() {
        return contrianReferences;
    }

    /**
     * Define el valor de la propiedad contrianReferences.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setContrianReferences(String value) {
        this.contrianReferences = value;
    }

    /**
     * Obtiene el valor de la propiedad hideSize.
     */
    public boolean isHideSize() {
        return hideSize;
    }

    /**
     * Define el valor de la propiedad hideSize.
     */
    public void setHideSize(boolean value) {
        this.hideSize = value;
    }

    /**
     * Obtiene el valor de la propiedad languageType.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getLanguageType() {
        return languageType;
    }

    /**
     * Define el valor de la propiedad languageType.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setLanguageType(String value) {
        this.languageType = value;
    }

    /**
     * Constructor VisualDatabaseType
     */
    public VisualDatabaseType() {
        super();
    }
}
