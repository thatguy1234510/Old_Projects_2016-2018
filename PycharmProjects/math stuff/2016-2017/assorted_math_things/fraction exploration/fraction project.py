import matplotlib.pyplot as plt

def frac(x):
    try:
        return 1+(1/x)
        # send back the next version of it
    except ZeroDivisionError:
        return False
        # make errors obvious

iterates=[]
# store some iterations of plotting
running_count=1
# start a running count at 1
for i in range(25):
    running_count=frac(running_count)
    # update the running count
    iterates.append(running_count)
    # add to list for plotting
    print(running_count)
    # print it
plt.plot(range(len(iterates)), iterates)
plt.show()
# plot the progress
