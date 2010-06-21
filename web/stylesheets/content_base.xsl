<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fn="http://www.w3.org/2006/xpath-function"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns="http://www.w3.org/1999/xhtml">

  <xsl:output method="text" indent="no" encoding="UTF-8" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:param name="witNum" tunnel="no"/>


  <xsl:variable name="blank">
	<xsl:text> </xsl:text>
  </xsl:variable>


  <xsl:template match="/">
	<html>
	  <head>
		<xsl:apply-templates select="//*:head"/>
	  </head>

	  <body>
		<xsl:choose>
		  <!--
		  il testo e` in versi
		  -->
		  <xsl:when test="//*:l">
			<xsl:apply-templates select="//*:l"/>
		  </xsl:when>
		  <!--
		  il testo non e` in versi
		  -->
		  <xsl:otherwise>
			<xsl:apply-templates select="//*:p">
			  <xsl:with-param name="radix"/>
			</xsl:apply-templates>
		  </xsl:otherwise>
		</xsl:choose>
	  </body>
	</html>
  </xsl:template>


  <!-- il titolo del testo -->
  <xsl:template match="*:head">

	<xsl:choose>
	  <!--
	  il titolo puo` essere dipendente dal valore del testimone
	  -->
	  <xsl:when test="//*:witList">
		<xsl:for-each select=".//rdg[contains(@wit, $witNum)]">
		  <h3>
			<xsl:apply-templates select="./node()">
			  <xsl:with-param name="radix" select="./name()"/>
			</xsl:apply-templates>
		  </h3>
		</xsl:for-each>
	  </xsl:when>

	  <!--
	  il titolo non dipende dal valore del(l'eventuale) testimone
	  inoltre e` indipendente dal fatto che il documento sia in versi o in prosa
	  -->
	  <xsl:otherwise>
		<h3>
		  <xsl:apply-templates/>
		</h3>
	  </xsl:otherwise>
	</xsl:choose>
  </xsl:template>
  

  <!-- ogni riga del testo -->
  <xsl:template match="*:l[@n]">
	<!--
	visualizzo anche le righe assenti per il dato witness
	-->
	<xsl:apply-templates select="./node()">
	  <xsl:with-param name="radix" select="./name()"/>
	</xsl:apply-templates>

	<!--
	<xsl:for-each select=".//rdg[contains(@wit, $witNum)]">
	  x
	  possono esserci piu` "rdg" in una sola "l".
	  In tal caso, aggiungere un blank dopo il contenuto
	  x
	  <xsl:if test="(position() &gt; 1) and (position() != last())">
		<xsl:copy-of select="$blank"/>
	  </xsl:if>

	  x
	  <xsl:value-of select="./name()"/>
	  <xsl:copy-of select="$blank"/>
	  x

	  <xsl:apply-templates select="./node()">
		<xsl:with-param name="radix" select="./name()"/>
	  </xsl:apply-templates>

	</xsl:for-each>
	-->

  </xsl:template>
  

  <!-- app -->
  <xsl:template match="*:app">
	<xsl:apply-templates select="./node()">
	  <xsl:with-param name="radix" select="./name()"/>
	</xsl:apply-templates>
  </xsl:template>


  <!-- rdg -->
  <xsl:template match="*:rdg">
	<xsl:if test="contains(@wit, $witNum)">
	  <!--
	  possono esserci piu` "rdg" in una sola "l".
	  In tal caso, aggiungere un blank dopo il contenuto
	  -->
	  <xsl:if test="(position() &gt; 1) and (position() != last())">
		<xsl:copy-of select="$blank"/>
	  </xsl:if>

	  <xsl:apply-templates select="./node()">
		<xsl:with-param name="radix" select="./name()"/>
	  </xsl:apply-templates>
	</xsl:if>
  </xsl:template>


  <!-- del -->
  <xsl:template match="*:del">
	<xsl:param name="radix" select="'assente'"/>
	<xsl:if test="$radix != 'assente'">
	  <xsl:if test="node()[(ancestor::l) or (ancestor::title) or (ancestor::p)]">
		<xsl:copy-of select="$blank"/>
		<!--del--><xsl:value-of select="."/><!--/del-->
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	</xsl:if>
  </xsl:template>


  <!-- add -->
  <xsl:template match="*:add">
	<xsl:param name="radix" select="'assente'"/>
	<xsl:if test="$radix != 'assente'">
	  <xsl:if test="node()[(ancestor::l) or (ancestor::title) or (ancestor::p)]">
		<xsl:copy-of select="$blank"/>
		<!--em--><xsl:value-of select="."/><!--/em-->
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	</xsl:if>
  </xsl:template>


  <!-- hi -->
  <xsl:template match="*:hi">
	<xsl:param name="radix" select="'assente'"/>
	<xsl:if test="$radix != 'assente'">
	  <xsl:if test="node()[(ancestor::l) or (ancestor::title) or (ancestor::p)]">
		<xsl:copy-of select="$blank"/>
		<!--u--><xsl:value-of select="."/><!--/u-->
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	</xsl:if>
  </xsl:template>


  <!-- p -->
  <xsl:template match="*:p">
	<xsl:param name="radix" select="'assente'"/>
	  <!--p-->
		<xsl:apply-templates>
		  <xsl:with-param name="radix" select="./name()"/>
		</xsl:apply-templates>
	  <!--/p-->
  </xsl:template>

</xsl:stylesheet>
