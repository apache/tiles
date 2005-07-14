/*
 * $Id$ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.webapp.tiles.invoice;

/**
 * An address.
 */
public class Address
{

  /**
   * Street part 1
   */
  private String street1;

  /**
   * Street part 2
   */
  private String street2;

  /**
   * City
   */
  private String city;

  /**
   * Country
   */
  private String country;

  /**
   * Zip Code
   */
  private String zipCode;

  public Address()
  {
  }

  /**
* Access method for the street1 property.
*
* @return   the current value of the street1 property
   */
  public String getStreet1() {
    return street1;}

  /**
   * @return void
* Sets the value of the street1 property.
*
* @param aStreet1 the new value of the street1 property
   */
  public void setStreet1(String aStreet1) {
    street1 = aStreet1;}

  /**
* Access method for the street2 property.
*
* @return   the current value of the street2 property
   */
  public String getStreet2() {
    return street2;}

  /**
   * @return void
* Sets the value of the street2 property.
*
* @param aStreet2 the new value of the street2 property
   */
  public void setStreet2(String aStreet2) {
    street2 = aStreet2;}

  /**
* Access method for the city property.
*
* @return   the current value of the city property
   */
  public String getCity() {
    return city;}

  /**
   * @return void
* Sets the value of the city property.
*
* @param aCity the new value of the city property
   */
  public void setCity(String aCity) {
    city = aCity;}

  /**
* Access method for the country property.
*
* @return   the current value of the country property
   */
  public String getCountry() {
    return country;}

  /**
   * @return void
* Sets the value of the country property.
*
* @param aCountry the new value of the country property
   */
  public void setCountry(String aCountry) {
    country = aCountry;}

  /**
* Access method for the zipCode property.
*
* @return   the current value of the zipCode property
   */
  public String getZipCode() {
    return zipCode;}

  /**
   * @return void
* Sets the value of the zipCode property.
*
* @param aZipCode the new value of the zipCode property
   */
  public void setZipCode(String aZipCode) {
    zipCode = aZipCode;}
}
