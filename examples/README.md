# Creating a plugin

There are 3 folders given:
- `/plugins`: containing example of existing plugins in the system in the form of zip file.
- `/interfaces`: containing simplified abstraction for each interface as extension point to what the user has to implement.
- `/templates`: containing the template to help creating a new plugin.

Steps to create a plugin:
- Put all required jars in `/jars` directory.
- Copy required component's template or feature's template from `/templates` directory.
- Start coding from the copied template.

2 ways to integrate a plugin:
- Plugins can be copied directly to the `/plugins` directory. or 
- Plugins can be loaded through GUI which will copy plugins to `/plugins` directory by zipping java file and inserting from Load Plugins section or Add more feature section.