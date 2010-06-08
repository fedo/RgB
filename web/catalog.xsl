<?xml version="1.0" encoding="UTF-8"?>

<!--
	Document   : catalog.xsl
    Author     : illbagna
    Description: XSL stylesheet for the data retrieval associated to the hover event
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html" indent="yes" encoding="UTF-8"/>

  <xsl:variable name="newline">
	<br />
  </xsl:variable>

  <xsl:variable name="comma">
	<xsl:text>, </xsl:text>
  </xsl:variable>

  <xsl:variable name="colon">
	<xsl:text>: </xsl:text>
  </xsl:variable>

  <xsl:template match="/">
     <html>
        <head>
           <title><xsl:text>Phantoms</xsl:text></title>
           <!--
           <style type="text/css">
              @import url(style.css);
           </style>
           -->
        </head>

        <body>
           <div id="progetto">
              <!--
              <img id="p_immagine" src="phantom_mask.gif" alt="phantom"/>
              -->
           
              <!-- il progetto -->
              <div id="p_nome">
                 <xsl:text>Phantoms</xsl:text>
                 <!-- TODO aggiungere il link -->
              </div>
           </div>

           <!-- i servizi -->
           <div id="servizi">
              <div class="sezione">
                 <xsl:text>i servizi</xsl:text>
                 <xsl:copy-of select="$colon"/>
              </div>
              <div id="tabella">
              
                 <table border="1">
                    <tr>
                       <th><xsl:text>n.</xsl:text></th>
                       <th><xsl:text>Servizio</xsl:text></th>
                       <th><xsl:text>Descrizione</xsl:text></th>
                       <th><xsl:text>URI</xsl:text></th>
                       <th><xsl:text>Metodo HTTP</xsl:text></th>
                       <th><xsl:text>Parametri</xsl:text></th>
                       <th><xsl:text>Formati di Input</xsl:text></th>
                       <th><xsl:text>Formati di Output</xsl:text></th>
                    </tr>
                    <xsl:for-each select="//service">
                       <tr>
                          <td><xsl:value-of select="position()"/></td>
                          <td><xsl:value-of select="name"/></td>
                          <td><xsl:value-of select="description"/></td>
                          <td><xsl:value-of select="URI"/></td>
                          <td><xsl:value-of select="HTTPmethod"/></td>
                          <td>
                             <xsl:call-template name="p_elem">
                                <xsl:with-param name="pp" select="params"/>
                             </xsl:call-template>
                          </td>
                          <td>
                             <xsl:call-template name="p_elem">
                                <xsl:with-param name="pp" select="inputs"/>
                             </xsl:call-template>
                          </td>
                          <td>
                             <xsl:call-template name="p_elem">
                                <xsl:with-param name="pp" select="outputs"/>
                             </xsl:call-template>
                          </td>
                       </tr>
                    </xsl:for-each>
                 </table>
              </div>
           </div>

           <!-- il gruppo -->
           
           <div class="sezione" id="gruppo">
              <xsl:value-of select="//group"/>
              <xsl:copy-of select="$colon"/>
              <!-- membri -->
              <div class="membro">
                 <xsl:for-each select="//member">
                    <xsl:if test="position() &gt; 1">
                       <xsl:copy-of select="$comma"/>
                    </xsl:if>
                    <xsl:value-of select="."/>
                 </xsl:for-each>
              </div>
           </div>
        </body>
     </html>
  </xsl:template>



  <xsl:template name="p_elem">
     <xsl:param name="pp"/>
     <xsl:choose>
     <xsl:when test="count($pp/*) &gt; 0">
     <xsl:for-each select="$pp/*">
        <xsl:if test="position() &gt; 1">
           <xsl:copy-of select="$comma"/>
        </xsl:if>
        <xsl:value-of select="."/>
     </xsl:for-each>
     </xsl:when>
     <xsl:otherwise>
        <xsl:value-of select="$pp"/>
     </xsl:otherwise>
     </xsl:choose>
  </xsl:template>

  
  <xsl:template match="group">
     <div class="gruppo">
        <xsl:value-of select="."/>
     </div>
  </xsl:template>

  <!--
  <xsl:template match="members">
     <div class="membri">
        <xsl:for-each select="//member">
           <xsl:value-of select="."/>--> <!-- TODO inserire il link alla pagina personale --><!--
        </xsl:for-each>
     </div>
  </xsl:template>-->

  <!--
  <xsl:template match="service">
     <xsl:for-each select=".">
        <tr>
           <td><xsl:value-of select="name"/></td>
           <td><xsl:value-of select="description"/></td>
           <td><xsl:value-of select="URI"/></td>
           <td><xsl:value-of select="HTTPmethod"/></td>
           <td><xsl:value-of select="params"/></td>
           <td>
              <xsl:for-each select="inputs">
                 <xsl:value-of select="input"/>
              </xsl:for-each>
           </td>
           <td>
              <xsl:for-each select="outputs">
                 <xsl:value-of select="output"/>
              </xsl:for-each>
           </td>
        </tr>
     </xsl:for-each>
  </xsl:template>-->

  <!--
  <xsl:template name="heading">
     <tr>
        <th><xsl:text>servizio</xsl:text></th>
        <th><xsl:text>descrizione</xsl:text></th>
        <th><xsl:text>URI</xsl:text></th>
        <th><xsl:text>metodo HTTP</xsl:text></th>
        <th><xsl:text>parametri</xsl:text></th>
        <th><xsl:text>formati di input</xsl:text></th>
        <th><xsl:text>formati di output</xsl:text></th>
     </tr>
  </xsl:template>-->

</xsl:stylesheet>
