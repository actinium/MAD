################################################################################
# MAD
################################################################################

Java( target = 'database/build', source = 'database/src')
Clean('.', 'database/build')

Jar( target = 'database/dist/MAD.jar', source = ['database/build','Manifest.txt'], JARCHDIR='$SOURCE' )
Clean('.', 'database/dist')
Command("MAD.jar", "database/dist/MAD.jar", Copy("$TARGET", "$SOURCE"))

