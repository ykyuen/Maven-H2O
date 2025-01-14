CREATE TABLE partnership (
	partnership_id varchar NOT NULL,
	subject varchar,
	recipient_address varchar,
	receipt_address varchar,
	is_sync_reply varchar,
	is_receipt_requested varchar,
	is_outbound_sign_required varchar,
	is_outbound_encrypt_required varchar,
	is_outbound_compress_required varchar,
	is_receipt_sign_required varchar,
	is_inbound_sign_required varchar,
	is_inbound_encrypt_required varchar,
	sign_algorithm varchar,
	encrypt_algorithm varchar,
	mic_algorithm varchar,
	as2_from varchar NOT NULL,
	as2_to varchar NOT NULL,
	encrypt_cert binary,
	verify_cert binary,
	retries int,
	retry_interval int,
	is_disabled varchar NOT NULL,
	is_hostname_verified varchar,
	PRIMARY KEY (partnership_id)
);

CREATE TABLE message (
	message_id varchar NOT NULL,
	message_box varchar NOT NULL,
	as2_from varchar NOT NULL,
	as2_to varchar NOT NULL,
	is_receipt varchar,
	is_acknowledged varchar,
	is_receipt_requested varchar,
	receipt_url varchar,
	mic_value varchar,
	original_message_id varchar, -- message_id of the message that the receipt replies to 
	primal_message_id varchar,-- The message_id of message which triggered "Resend as New Message"
	has_resend_as_new varchar,
	partnership_id varchar,
	time_stamp timestamp NOT NULL,
	status varchar NOT NULL,
	status_desc varchar,
	PRIMARY KEY (message_id, message_box)
);

CREATE TABLE repository (
	message_id varchar NOT NULL,
	message_box varchar NOT NULL,
	content binary NOT NULL,
	PRIMARY KEY (message_id, message_box)
);

CREATE TABLE raw_repository (
	message_id varchar NOT NULL,
	content binary NOT NULL,
	PRIMARY KEY (message_id)
);