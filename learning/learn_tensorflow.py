import tensorflow as tf

"""
Think of it as two sections:
    1. Building the computational graph
    2. Running the computational graph
    
 
"""

node1 = tf.constant(3.0, dtype=tf.float32)
node2 = tf.constant(4.0)

# To evaluate the nodes we need to run them in a session
sess = tf.Session()

# To build more complicated systems combine nodes with operations (which are also nodes)
# A placeholder is a promise to provide a value later as an external input
# A variable is a trainable parameter.

W = tf.Variable([0.3])
b = tf.Variable([-0.3])
x = tf.placeholder(tf.float32)
linear_model = W*x+b
init = tf.global_variables_initializer()
sess.run(init)
y = tf.placeholder(tf.float32)
squared_deltas = tf.square(linear_model-y)
loss = tf.reduce_sum(squared_deltas)

