<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<tiles:insert template='/test/templateLayout.jsp'>
  <tiles:put name="title"  content="My first page" direct="true"/>
  <tiles:put name="header" content="/common/header.jsp" direct="true"/>
  <tiles:put name="footer" content="/common/footer.jsp" />
  <tiles:put name="menu"   content="/basic/menu.jsp" direct="true"/>
   <tiles:put name="body" content='/testAction.do' type="page"/>
</tiles:insert>

<%--
   <tiles:put name="body" content='/testAction2.do'/>
   <tiles:put name="body" direct='true'>
      <bean:insert id='xout2' page='/forwardExampleAction.do'/>
      <bean:write name='xout2' filter='false'/>
    </tiles:put>
--%>
