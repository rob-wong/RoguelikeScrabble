package com.example.gymapprefactor.business.errors

sealed class StorageException(message: String, cause: Throwable? = null) : Exception(message, cause) {
	data class IOException(
		val operation: String,
		override val cause: java.io.IOException
	) : StorageException("Failed to $operation: ${cause.message}", cause)
	
	data class SerializationException(
		val operation: String,
		override val cause: Throwable
	) : StorageException("Failed to serialize/deserialize during $operation: ${cause.message}", cause)
	
	data class CorruptedDataException(
		val filePath: String
	) : StorageException("Data file is corrupted: $filePath")
	
	data class PermissionException(
		val operation: String,
		override val cause: SecurityException
	) : StorageException("Permission denied for $operation: ${cause.message}", cause)
}
