# Dual Profiles ProGuard Rules

# Keep device admin receiver
-keep class com.example.dualprofiles.admin.MyDeviceAdminReceiver { *; }

# Keep provisioning activities
-keep class com.example.dualprofiles.ui.ProvisioningModeActivity { *; }
-keep class com.example.dualprofiles.ui.PolicyComplianceActivity { *; }

# Keep cross-profile activities
-keep class com.example.dualprofiles.ui.SwitchProfileActivity { *; }
-keep class com.example.dualprofiles.launcher.FocusLauncherActivity { *; }
