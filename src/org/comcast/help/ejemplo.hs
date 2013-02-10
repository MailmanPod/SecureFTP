<?xml version="1.0" encoding="ISO-8859-1"?>
<helpset>
	<title>Ayuda - Secure FTP</title>
		<maps>
			<homeID>presentacion</homeID>		<!-- P�gina por defecto al mostrar la ayuda -->
			<mapref location="ejemplo.jhm"/>	<!-- Que mapa deseamo -->
		</maps>

		<!-- Las Vistas que deseamos mostrar en la ayuda -->
		<view>						<!-- Deseamos una tabla de contenidos -->
			<name>Content</name>
			<label>Tabla de contenidos</label>	<!-- El tooltiptext -->
			<type>javax.help.TOCView</type>
			<image>ContentIco</image>		<!-- El icono que se muesta -->	
			<data>ejemploTOC.xml</data>		<!-- El fichero que la define -->
		</view>

		<!--<view xml:lang="es">				<!-- Deseamos que se puedan realizar b�squedas -->
		<!--	<name>Search</name>
			<label>B�squeda</label>			<!-- El tooltiptext -->
		<!--	<image>SearchIco</image>		<!-- El icono que se muesta -->
		<!--	<type>javax.help.SearchView</type>
			<data engine="com.sun.javax.help.search.DefaultSearchEngine">JavaHelpSearch</data>
		</view>-->

		<!-- Definici�n de la ventana principal de la ayuda-->
		<presentation default="true" displayviews="true" displayviewimages="true">
			<name>MainWin</name>
			<size width="640" height="480"/>		<!-- Dimensiones iniciales -->
			<location x="200" y="200"/>			<!-- Posici�n inicial -->
			<title>Ayuda - Secure FTP</title> <!-- T�tulo de la ventana -->
			<toolbar>	<!-- Definimos la barra de herramientas de la ventana -->
				<!-- Permitimos ir a la p�gina anterior -->
				<helpaction image="BackwardIco">javax.help.BackAction</helpaction>
				<!-- Permitimos ir a la p�gina siguiente -->
				<helpaction image="ForwardIco">javax.help.ForwardAction</helpaction>
				<!-- Permitimos imprimir el contenido -->
				<helpaction image="PrintIco">javax.help.PrintAction</helpaction>
				<!-- Permitimos configurar la impresi�n -->
				<helpaction image="PrintSetupIco">javax.help.PrintSetupAction</helpaction>
			</toolbar>
		</presentation>
</helpset>