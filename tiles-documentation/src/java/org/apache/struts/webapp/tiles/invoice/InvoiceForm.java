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

import org.apache.struts.action.ActionForm;


/**
 */

public final class InvoiceForm extends ActionForm {

  /**
   * Shipping address
   */
  private Address shippingAddress;

  /**
   * Bill address
   */
  private Address billAddress;

  /**
   * Invoice total amount
   */
  private double amount;

  /**
   * Customer firstname
   */
  private String firstname;

  /**
   * Customer last name
   */
  private String lastname;

  public InvoiceForm()
    {
    shippingAddress = new Address();
    billAddress = new Address();
    }

  /**
   * Access method for the shippingAddress property.
   *
   * @return   the current value of the shippingAddress property
   */
  public Address getShippingAddress()
   {
    return shippingAddress;}

  /**
   * @return void
   * Sets the value of the shippingAddress property.
   *
   * @param aShippingAddress the new value of the shippingAddress property
   */
  public void setShippingAddress(Address aShippingAddress)
    {
    shippingAddress = aShippingAddress;
    }

  /**
   * Access method for the billAddress property.
   *
   * @return   the current value of the billAddress property
   */
  public Address getBillAddress()
    {
    return billAddress;
    }

  /**
   * @return void
   * Sets the value of the billAddress property.
   *
   * @param aBillAddress the new value of the billAddress property
   */
  public void setBillAddress(Address aBillAddress)
    {
    billAddress = aBillAddress;
    }

  /**
   * Access method for the amount property.
   *
   * @return   the current value of the amount property
   */
  public double getAmount()
    {
    return amount;
    }

  /**
   * @return void
   * Sets the value of the amount property.
   *
   * @param aAmount the new value of the amount property
   */
  public void setAmount(double aAmount)
    {
    amount = aAmount;
    }

  /**
   * Access method for the firstname property.
   *
   * @return   the current value of the firstname property
   */
  public String getFirstname()
    {
    return firstname;
    }

  /**
   * @return void
   * Sets the value of the firstname property.
   *
   * @param aFirstname the new value of the firstname property
   */
  public void setFirstname(String aFirstname)
    {
    firstname = aFirstname;
    }

  /**
   * Access method for the lastname property.
   *
   * @return   the current value of the lastname property
   */
  public String getLastname()
    {
    return lastname;
    }

  /**
   * @return void
   * Sets the value of the lastname property.
   *
   * @param aLastname the new value of the lastname property
   */
  public void setLastname(String aLastname)
    {
    lastname = aLastname;
    }

}

