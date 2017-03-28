# ExpressPOS

#Git Config Instructions

Set name: git config --global user.name <<>>

Set email: git config --global user.email <<>>

#Commiting Instructions

First, we'll make sure we're up to date with the staging branch using the following command: git pull staging

We will then change branch to staging. To change from our current branch to staging, we execute the following: git checkout staging

Create your feature branch, the feature branch name should be descriptive of the feature you are adding to the project. To do this, run: git checkout -b <<>>

Next, you will push the branch to the git repository. git push origin <<>>

Make the changes to the application, making commits as necessary (git commit -m <<>>). Make sure you write JavaDoc comments as necessary. I will not merge your branches if you do not have this. When complete the feature, merge the staging branch into your branch. You will probably end up with a merge conflict. Resolve the conflict. Ensure the application compiles. Make a commit after you resolve the merge conflict.

Finially, create a pull request for your branch to the staging branch. Make sure you watch the pull request page for my feedback.

#Create issues

Think about what we need to create in this task. Don't write only common task, like 'Create menu screen'. If you want to create new issue topic:

First, write detail requirements for this task. What should be done before we start this task. Write all classes, functionality we need for the specific task.

Next, if UI is requered for this task, provide UI scratch drawn on paper/photoshop/other UI design software. (UI can be changed anytime in future)

Finally, try to be very specific in the description of the task.

We need this for more understanding what we are doing and if somebody starts some task, he knows exactly what he needs to create and don't need to guess what we need to do in this task.
